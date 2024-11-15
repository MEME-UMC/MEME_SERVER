package org.meme.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.meme.auth.common.ErrorReasonDto;
import org.meme.auth.common.status.ErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ErrorReasonDto errorReasonDto = ErrorReasonDto.builder()
                .httpStatus(ErrorStatus._FORBIDDEN.getHttpStatus())
                .code(ErrorStatus._FORBIDDEN.getCode())
                .message(ErrorStatus._FORBIDDEN.getMessage())
                .success(false)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorReasonDto);
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
