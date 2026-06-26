package com.studylib.common.exception;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.vo.ApiResponse;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
    return ResponseEntity.status(resolveStatus(ex.getCode()))
        .body(ApiResponse.failure(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + " " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    return ResponseEntity.badRequest().body(ApiResponse.failure(ErrorCodeConstants.INVALID_PARAM, message));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ApiResponse<Object>> handleBindException(BindException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + " " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    return ResponseEntity.badRequest().body(ApiResponse.failure(ErrorCodeConstants.INVALID_PARAM, message));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleUnknownException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure(ErrorCodeConstants.SYSTEM_ERROR, ex.getMessage() == null ? "Internal server error" : ex.getMessage()));
  }

  private HttpStatus resolveStatus(int code) {
    if (code == ErrorCodeConstants.UNAUTHORIZED) {
      return HttpStatus.UNAUTHORIZED;
    }
    if (code == ErrorCodeConstants.FORBIDDEN) {
      return HttpStatus.FORBIDDEN;
    }
    if (code == ErrorCodeConstants.NOT_FOUND) {
      return HttpStatus.NOT_FOUND;
    }
    if (code == ErrorCodeConstants.INVALID_PARAM) {
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.BAD_REQUEST;
  }
}
