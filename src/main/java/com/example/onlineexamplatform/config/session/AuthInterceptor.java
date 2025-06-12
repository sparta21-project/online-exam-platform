package com.example.onlineexamplatform.config.session;

import java.io.IOException;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.user.entity.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest req,
		HttpServletResponse res,
		Object handler) throws IOException {

		// 컨트롤러 메서드 인지 확인
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod hm = (HandlerMethod)handler;
		// @CheckAuth 어노테이션이 있는지 확인
		CheckAuth anno = hm.getMethodAnnotation(CheckAuth.class);
		if (anno == null) {
			// 없으면 건너뛰기
			return true;
		}

		// request에 있는 userSession 꺼내기
		UserSession session = (UserSession)req.getAttribute("userSession");
		if (session == null) {
			// 없으면 401 UNAUTHORIZED 반환
			throw new ApiException(ErrorStatus.UNAUTHORIZED);
		}

		// 어노테이션에 지정된 Role의 값이랑 세션에 있는 Role이 값이 같은지 비교
		Role required = anno.value();
		Role actual = session.getRole();

		// USER = 0 , ADMIN = 1 이고 USER가 되는건 ADMIN도 되게 허용
		if (actual.ordinal() < required.ordinal()) {
			// 권한이 USER인데 ADMIN전용 api을 접근할 경우 403 Forbidden 반환
			throw new ApiException(ErrorStatus.FORBIDDEN);
		}

		return true;
	}
}
