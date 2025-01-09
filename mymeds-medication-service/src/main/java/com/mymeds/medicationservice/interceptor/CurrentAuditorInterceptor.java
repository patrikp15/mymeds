package com.mymeds.medicationservice.interceptor;

import com.mymeds.medicationservice.security.CurrentAuditorContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CurrentAuditorInterceptor implements HandlerInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(CurrentAuditorInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    LOG.info("action=preHandle, status=started");
    final String userId = request.getHeader("x-user-key");
    if (userId != null) {
      LOG.info("action=preHandle, message=setting userId={} to status=CurrentAuditorContextHolder", userId);
      CurrentAuditorContextHolder.set(userId);
    }
    LOG.info("action=preHandle, status=finished");
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    LOG.info("action=afterCompletion, status=started");
    CurrentAuditorContextHolder.clear();
    if (CurrentAuditorContextHolder.get().isEmpty()) {
      LOG.info("action=afterCompletion, status=CurrentAuditorContextHolder has been cleared");
    }
    LOG.info("action=afterCompletion, status=finished");
  }
}
