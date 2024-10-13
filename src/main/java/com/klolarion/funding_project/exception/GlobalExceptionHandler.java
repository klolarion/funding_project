package com.klolarion.funding_project.exception;

import com.klolarion.funding_project.exception.base_exceptions.DataAccessFailException;
import com.klolarion.funding_project.exception.base_exceptions.ResourceNotFoundException;
import com.klolarion.funding_project.exception.base_exceptions.ValidationException;
import com.klolarion.funding_project.exception.funding.FundingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.ServiceUnavailableException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //추후 고려 -> 더 자세한 예외처리
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
//        Map<String, String> errorDetails = new HashMap<>();
//        errorDetails.put("errorCode", "INTERNAL_SERVER_ERROR");
//        errorDetails.put("errorMessage", "서버 오류가 발생했습니다.");
//        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    /*전역 500*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception e) {
        log.error("서버 오류 발생: " + e);
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + e.getMessage());
    }

    /*404*/
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /*400*/
    @ExceptionHandler(FundingException.class)
    public ResponseEntity<String> handleBadRequestExceptions(FundingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /*409 중복, 검증 실패*/
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationExceptions(ValidationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /*503 서비스 사용 불가*/
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<String> handleServiceUnavailableExceptions(ServiceUnavailableException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }

    /*500 데이터 접근, 처리 실패*/
    @ExceptionHandler(DataAccessFailException.class)
    public ResponseEntity<String> handleDataAccessExceptions(DataAccessFailException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터 처리 중 오류가 발생했습니다.");
    }




}
