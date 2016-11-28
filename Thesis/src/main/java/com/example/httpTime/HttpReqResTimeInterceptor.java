package com.example.httpTime;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class HttpReqResTimeInterceptor extends HandlerInterceptorAdapter{

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Instant responseTimeInstance = Instant.now();
		long responseTime = responseTimeInstance.toEpochMilli()-(Long)request.getAttribute("requestStartTime");
		System.err.println("response time with rendering view: "+responseTime);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Instant responseTimeInstance = Instant.now();
		long responseTime = responseTimeInstance.toEpochMilli()-(Long)request.getAttribute("requestStartTime");
		System.err.println("response time without rendering view: "+responseTime);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long requestTimeInstance = Instant.now().toEpochMilli();
		request.setAttribute("requestStartTime", requestTimeInstance);
		System.err.println("request method type: "+request.getMethod());
		return true;
	}
	
	

}
