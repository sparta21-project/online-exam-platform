package com.example.onlineexamplatform.domain.exam.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.entity.BaseEntity;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.dto.request.UpdateExamRequestDto;
import com.example.onlineexamplatform.domain.examAnswer.entity.ExamAnswer;
import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;
import com.example.onlineexamplatform.domain.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@Table(name = "exam")
@NoArgsConstructor
@AllArgsConstructor
public class Exam extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Long totalQuestionsNum;

	@Column(nullable = false)
	private LocalDateTime startTime;

	@Column(nullable = false)
	private LocalDateTime endTime;

	@Column
	private Integer remainUsers;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ExamFile> examFiles = new ArrayList<>();

	@OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ExamAnswer> examAnswers = new ArrayList<>();

	public void updateExam(UpdateExamRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.description = requestDto.getDescription();
		this.startTime = requestDto.getStartTime();
		this.endTime = requestDto.getEndTime();
	}

	public void decreaseRemainUsers() {
		if(this.remainUsers == null) return;

		if(this.remainUsers == 0) throw new ApiException(ErrorStatus.USER_NOT_FOUND);

		this.remainUsers --;
	}
}
