package com.khaai.NuoiEm.Handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		 String errorMessage = "Tên đăng nhập hoặc mật khẩu không đúng";
	     request.getSession().setAttribute("errorLoginMessage", errorMessage);
	     response.sendRedirect("/login?error");
		
	}
}
