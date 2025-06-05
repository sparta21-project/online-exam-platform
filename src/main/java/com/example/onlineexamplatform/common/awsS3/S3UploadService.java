package com.example.onlineexamplatform.common.awsS3;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

	private final S3Client s3Client;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	// [public 메서드] 외부에서 사용, S3에 저장된 이미지 객체의 public url을 반환
	public List<String> upload(List<MultipartFile> files) {
		// 각 파일을 업로드하고 url을 리스트로 반환
		return files.stream()
			.map(this::uploadImage)
			.toList();
	}

	// [private 메서드] validateFile메서드를 호출하여 유효성 검증 후 uploadImageToS3메서드에 데이터를 반환하여 S3에 파일 업로드, public url을 받아 서비스 로직에 반환
	private String uploadImage(MultipartFile file) {
		validateFile(file.getOriginalFilename()); // 파일 유효성 검증
		return uploadImageToS3(file); // 이미지를 S3에 업로드하고, 저장된 파일의 public url을 서비스 로직에 반환
	}

	// [private 메서드] 파일 유효성 검증
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
		List<String> allowedExtentionList = Arrays.asList("image/jpg", "image/jpeg", "image/png", "image/gif");
		if (!allowedExtentionList.contains(extension)) {
			throw new ApiException(ErrorStatus.INVALID_FILE_EXTENSION);
		}
	}

	// [private 메서드] 직접적으로 S3에 업로드
	private String uploadImageToS3(MultipartFile file) {
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
				.contentType("image/" + extension) // 이미지 MIME 타입
				.contentLength(file.getSize()) // 파일 크기
				.build();
			// S3에 이미지 업로드
			s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			throw new ApiException(ErrorStatus.IO_EXCEPTION_UPLOAD_FILE);
		}

		// public url 반환
		return s3FileName;
	}

}

