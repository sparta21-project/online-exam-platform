package com.example.onlineexamplatform.common.awsS3Util;

import java.util.Map;

public class MimeTypeUtil {

	private static final Map<String, String> MIME_TYPES = Map.ofEntries(
		Map.entry("jpg", "image/jpeg"),
		Map.entry("jpeg", "image/jpeg"),
		Map.entry("png", "image/png"),
		Map.entry("gif", "image/gif"),
		Map.entry("webp", "image/webp"),
		Map.entry("pdf", "application/pdf")
	);

	public static String getMimeType(String extension) {
		if (extension == null) {
			throw new IllegalArgumentException("파일 확장자가 null입니다.");
		}

		String lowerCaseExt = extension.toLowerCase();
		return MIME_TYPES.getOrDefault(lowerCaseExt, "application/octet-stream");
	}
}
