package com.example.onlineexamplatform.domain.examFile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.domain.examFile.service.ExamFileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class ExamFileController {

	private final ExamFileService examFileService;
}
