package com.example.onlineexamplatform.domain.examFile.service;

import static com.example.onlineexamplatform.common.awsS3Util.MimeTypeUtil.*;

import java.io.InputStream;
import java.net.URLConnection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamFileQueryRepository;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;
import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;
import com.example.onlineexamplatform.domain.examFile.repository.ExamFileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

	private final ExamFileQueryRepository examFileQueryRepository;
	private final ExamFileRepository examFileRepository;
	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	// 외부에서 사용, S3에 저장된 ExamFile 객체의 path를 반환
	public List<ExamFileResponseDto> upload(List<MultipartFile> files) {
		// 각 파일을 업로드하고 path를 리스트로 반환
		return files.stream()
			.map(this::uploadFile)
			.toList();
	}

	// validateFile 메서드를 호출하여 유효성 검증 후 uploadExamFileToS3메서드에 데이터를 반환하여 S3에 파일 업로드, path를 받아 서비스 로직에 반환
	private ExamFileResponseDto uploadFile(MultipartFile file) {
		validateFile(file.getOriginalFilename()); // 파일 유효성 검증
		return uploadExamFileToS3(file); // 이미지를 S3에 업로드하고, 저장된 파일의 path를 서비스 로직에 반환
	}

	// 파일 유효성 검증
	private void validateFile(String filename) {
		// 파일 존재 유무 검증
		if (filename == null || filename.isEmpty()) {
			throw new ApiException(ErrorStatus.NOT_EXIST_FILE);
		}

		// 확장자 존재 유무 검증
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new ApiException(ErrorStatus.NOT_EXIST_FILE_EXTENSION);
		}

		// 허용되지 않는 확장자 검증
		String extension = URLConnection.guessContentTypeFromName(filename);
		List<String> allowedExtentionList = Arrays.asList("image/jpg", "image/jpeg", "image/png", "image/gif",
			"application/pdf");
		if (!allowedExtentionList.contains(extension)) {
			throw new ApiException(ErrorStatus.INVALID_FILE_EXTENSION);
		}
	}

	// 직접적으로 S3에 업로드
	private ExamFileResponseDto uploadExamFileToS3(MultipartFile file) {
		// 원본 파일 명
		String originalFilename = file.getOriginalFilename();
		// 확장자 명
		String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".") + 1);
		// 변경된 파일
		String s3FileName = UUID.randomUUID() + "." + extension;

		// 이미지 파일 -> InputStream 변환
		try (InputStream inputStream = file.getInputStream()) {

			String mimeType = getMimeType(extension);

			// PutObjectRequest 객체 생성
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName) // 버킷 이름
				.key(s3FileName) // 저장할 파일 이름
				.contentType(mimeType) // 시험파일 MIME 타입
				.contentLength(file.getSize()) // 파일 크기
				.build();

			// S3에 시험파일 업로드
			s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
			// ExamFile Entity에 업로드된 파일 정보 저장
			ExamFile examFile = ExamFile.builder()
				.fileName(file.getOriginalFilename())
				.path(s3FileName)
				.fileType(file.getContentType())
				.size(file.getSize())
				.build();

			// 시험파일 DB에 저장
			examFileRepository.save(examFile);

			// path 반환
			return ExamFileResponseDto.of(examFile);

		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw new ApiException(ErrorStatus.IO_EXCEPTION_UPLOAD_FILE);
		}

	}

	public List<ExamFile> findAllByExamFileId(List<Long> examFileIds) {
		List<ExamFile> examFiles = examFileRepository.findAllById(examFileIds);
		if (examFiles.isEmpty())
			throw new ApiException(ErrorStatus.FILE_ID_MISSING);
		return examFiles;

	}

	public String createPresignedUrl(String s3FilePath, LocalDateTime endTime) {

		long duration = Duration.between(LocalDateTime.now(), endTime).toMinutes();
		log.info("PresignedUrl 유효시간 : {}분", duration);

		if (duration <= 0) {
			throw new ApiException(ErrorStatus.EXAM_ALREADY_ENDED);
		}

		PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
			getObjectRequest -> getObjectRequest.signatureDuration(
					Duration.ofMinutes(duration)) // 시험 시간 동안 만 PreSignedURL 유효
				.getObjectRequest(
					GetObjectRequest.builder()
						.bucket(bucketName)
						.key(s3FilePath)
						.build()
				)
		);
		return presignedGetObjectRequest.url().toString();
	}

	@Transactional
	public void deleteExamFile(List<ExamFile> examFiles) {
		// [1] 각 ExamFile 객체의 path를 S3에서 삭제할 키 목록 생성
		List<String> keys = getFullKeys(examFiles);

		try {
			// [2] 생성한 키 목록을 기반으로 S3에서 파일을 삭제하기 위한 요청 객체 생성
			DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
				.bucket(bucketName)
				.delete(delete -> delete.objects(
					keys.stream()
						.map(key -> ObjectIdentifier.builder().key(key).build())
						.toList()
				))
				.build();

			// [3] S3 및 DB ExamFile 제거
			s3Client.deleteObjects(deleteObjectsRequest);
			examFileRepository.deleteAll(examFiles);

		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw new ApiException(ErrorStatus.IO_EXCEPTION_DELETE_FILE);
		}
	}

	// ExamFile 객체의 path로 S3에서 삭제할 키 목록 생성
	private List<String> getFullKeys(List<ExamFile> examFiles) {
		return examFiles.stream()
			.map(ExamFile::getPath)
			.toList();
	}

	@Transactional
	public void deleteFilesByExam(Exam exam) {
		List<ExamFile> examFiles = examFileRepository.findByExamId(exam.getId());

		if (examFiles.isEmpty()) {
			log.info("시험 ID {} 에 연관된 파일이 없습니다.", exam.getId());
			return;
		}

		deleteExamFile(examFiles);
	}

	// examFile 고아객체 조회, examId가 null 인 examFile 중 createdAt이 주어진 기준(threshold)보다 오래된 것들만 조회
	public List<ExamFile> findOldUnlinkedExamFiles(LocalDateTime threshold) {
		return examFileQueryRepository.findOldUnlinkedExamFiles(threshold);
	}

}

