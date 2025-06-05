package com.example.onlineexamplatform.domain.examFile.service;

import org.springframework.stereotype.Service;

import com.example.onlineexamplatform.domain.examFile.repository.ExamFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamFileService {

	private final ExamFileRepository examFileRepository;

}
