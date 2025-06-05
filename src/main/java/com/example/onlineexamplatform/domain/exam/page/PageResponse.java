package com.example.onlineexamplatform.domain.exam.page;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PageResponse<T> {

	private final List<T> content;

	private final int page;

	private final int size;

	private final long totalElements;

	private final int totalPages;

	private final boolean first;

	private final boolean last;

	private final boolean empty;

	private final List<SortInfo> sort;

	public PageResponse(Page<T> page) {
		this.content = page.getContent();
		this.page = page.getNumber();
		this.size = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.first = page.isFirst();
		this.last = page.isLast();
		this.empty = page.isEmpty();
		this.sort = page.getSort().stream()
			.map(order -> new SortInfo(order.getProperty(), order.getDirection().name()))
			.collect(Collectors.toList());
	}

}
