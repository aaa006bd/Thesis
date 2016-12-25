package com.example.httpTime;

import java.io.FileWriter;
import java.io.Writer;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class HttpReqResTimeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Instant responseTimeInstance = Instant.now();
		long responseTime = responseTimeInstance.toEpochMilli() - (Long) request.getAttribute("requestStartTime");
		System.err.println("response time with rendering view: " + responseTime);
		writeTimeInfoToFile(responseTime, request.getMethod(), true);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Instant responseTimeInstance = Instant.now();
		long responseTime = responseTimeInstance.toEpochMilli() - (Long) request.getAttribute("requestStartTime");
		System.err.println("response time without rendering view: " + responseTime);
		writeTimeInfoToFile(responseTime, request.getMethod(), false);

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long requestTimeInstance = Instant.now().toEpochMilli();
		request.setAttribute("requestStartTime", requestTimeInstance);
		System.err.println("request method type: " + request.getMethod());

		return true;
	}

	public void writeTimeInfoToFile(long time, String requestType, boolean doesRenderValue) {
		try (Writer writer = new FileWriter("statistics.csv", true)) {

			if (requestType.equals("POST") && !doesRenderValue) {
				writer.write(time + ", ");
			}else if(requestType.equals("POST") && doesRenderValue){
				writer.write(time+"\n");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
