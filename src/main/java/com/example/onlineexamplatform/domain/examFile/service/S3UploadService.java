package com.example.onlineexamplatform.domain.examFile.service;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.repository.ExamFileQueryRepository;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;
import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;
import com.example.onlineexamplatform.domain.examFile.repository.ExamFileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

	private final ExamFileQueryRepository examFileQueryRepository;
	private final ExamFileRepository examFileRepository;
	private final S3Client s3Client;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	// 외부에서 사용, S3에 저장된 이미지 객체의 path를 반환
	public List<ExamFileResponseDto> upload(List<MultipartFile> files) {
		// 각 파일을 업로드하고 path를 리스트로 반환
		return files.stream()
			.map(this::uploadFile)
			.toList();
	}

	// validateFile 메서드를 호출하여 유효성 검증 후 uploadImageToS3메서드에 데이터를 반환하여 S3에 파일 업로드, path를 받아 서비스 로직에 반환
	private ExamFileResponseDto uploadFile(MultipartFile file) {
		validateFile(file.getOriginalFilename()); // 파일 유효성 검증
		return uploadImageToS3(file); // 이미지를 S3에 업로드하고, 저장된 파일의 path를 서비스 로직에 반환
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
	private ExamFileResponseDto uploadImageToS3(MultipartFile file) {
		// 원본 파일 명
		String originalFilename = file.getOriginalFilename();
		// 확장자 명
		String extension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".") + 1);
		// 변경된 파일
		String s3FileName = UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename;

		// 이미지 파일 -> InputStream 변환
		try (InputStream inputStream = file.getInputStream()) {
			// PutObjectRequest 객체 생성
			software.amazon.awssdk.services.s3.model.PutObjectRequest putObjectRequest = software.amazon.awssdk.services.s3.model.PutObjectRequest.builder()
				.bucket(bucketName) // 버킷 이름
				.key(s3FileName) // 저장할 파일 이름
				.contentType("application/" + extension) // 이미지 MIME 타입
				.contentLength(file.getSize()) // 파일 크기
				.build();

			// S3에 이미지 업로드
			s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw new ApiException(ErrorStatus.IO_EXCEPTION_UPLOAD_FILE);
		}

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
	}

	public List<ExamFile> findAllByImageId(List<Long> examFileIds) {
		List<ExamFile> examFiles = examFileRepository.findAllById(examFileIds);
		if (examFiles.isEmpty())
			throw new ApiException(ErrorStatus.IMAGE_ID_MISSING);
		return examFiles;

	}

	// 이미지의 path를 이용하여 S3에서 해당 이미지를 제거, getKeyFromImageAddress 메서드를 호출하여 삭제에 필요한 key 획득
	public void delete(List<String> imagePaths) {
		List<String> keys = imagePaths.stream()
			.map(this::getKeyFromImagePaths)
			.toList();

		try {
			// S3에서 파일을 삭제하기 위한 요청 객체 생성
			DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
				.bucket(bucketName) // S3 버킷 이름 지정
				.delete(delete -> delete.objects(
					// S3 객체들을 삭제할 객체 목록을 생성
					keys.stream()
						.map(key -> ObjectIdentifier.builder().key(key).build())
						.toList()
				))
				.build();
			s3Client.deleteObjects(deleteObjectsRequest); // S3에서 객체 삭제
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw new ApiException(ErrorStatus.IO_EXCEPTION_DELETE_FILE);
		}
	}

	// 삭제에 필요한 key 반환
	private String getKeyFromImagePaths(String imagePath) {
		try {
			URL url = new URI(imagePath).toURL(); // 인코딩된 주소를 Path 객체로 변환 후 Path 객체로 변환
			String decodedKey = URLDecoder.decode(url.getPath(),
				StandardCharsets.UTF_8);// URI에서 경로 부분을 가져와 URL 디코딩을 통해 실제 키로 변환
			return decodedKey.substring(1); // 경로 앞에 '/'가 있으므로 이를 제거한 뒤 반환
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw new ApiException(ErrorStatus.INVALID_URL_FORMAT);
		}
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

	// examFile 고아객체 조회, examId가 null 인 examFile 중 createdAt이 주어진 기준(threshold)보다 오래된 것들만 조회
	public List<ExamFile> findOldUnlinkedExamFiles(LocalDateTime threshold) {
		return examFileQueryRepository.findOldUnlinkedExamFiles(threshold);
	}

}

