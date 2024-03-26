package sa.tabadul.useraccessmanagement.exception;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;
import sa.tabadul.useraccessmanagement.common.models.response.ResponseEnvelope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandlerConfig {

	
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonCodes.DATE_TIME_STRIPPED);
	private static final int RANDOM_LOWER = 1000;
	private static final int RANDOM_UPPER = 9000;
	

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEnvelope<Exception> handleAccessDeniedExceptionError(HttpServletRequest request,
            HttpServletResponse response, AccessDeniedException ex) {
        // do something with request and response

        log.error(ExceptionMessage.EXCEPTION.getValue());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString();
        log.error(ExceptionMessage.STACKTRACE.getValue());
        log.error(sStackTrace);

        ResponseEnvelope<Exception> exceptionReponse = new ResponseEnvelope<>(ex.getCode(), ex.getMessage(),
                null);
        log.error(ExceptionMessage.EXCEPTION.getValue());
        return exceptionReponse;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEnvelope<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
    	log.error(ExceptionMessage.EXCEPTION.getValue());
        List<Object> errorList = new ArrayList<>();
        log.error(ExceptionMessage.STACKTRACE.getValue());

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            Map<String, String> validationError = new HashMap<>();
            validationError.put("key", ((FieldError) error).getField());
            validationError.put("message", error.getDefaultMessage());

            errorList.add(validationError);
        });
        log.error(errorList.toString());
        log.error(ExceptionMessage.EXCEPTION.getValue());

        return new ResponseEnvelope<Object>(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.VALIDATION_FAILED.getValue(),
                errorList);
    }
    
    @ExceptionHandler(GeneralException.class)
	public ResponseEntity<ResponseEnvelope<String>> handleGeneralException(HttpServletRequest request,
			HttpServletResponse response, GeneralException ex) {
	
    	
		ResponseEnvelope<String> exceptionReponse = new ResponseEnvelope<>(ex.getCode(), ex.getMessage(), null);
		response.setStatus(ex.getCode());
		return new ResponseEntity<ResponseEnvelope<String>>(exceptionReponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ResponseEnvelope<String>> handleBusinessExceptionExceptionError(HttpServletRequest request,
			HttpServletResponse response, BusinessException ex) {
		// do something with request and response
		generateLogs(request, response, ex, null);

		ResponseEnvelope<String> exceptionReponse = new ResponseEnvelope<>(ex.getCode(), ex.getMessage(), null);
		response.setStatus(ex.getCode());
		return new ResponseEntity<ResponseEnvelope<String>>(exceptionReponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseEnvelope<String>> handleGeneralError(HttpServletRequest request,
			HttpServletResponse response,

			Exception ex) {
		// do something with request and response

		String errorId = getErrorId();
		generateLogs(request, response, ex, errorId);
		ResponseEnvelope<String> exceptionReponse = new ResponseEnvelope<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				errorId, null);
		return new ResponseEntity<ResponseEnvelope<String>>(exceptionReponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public String getErrorId() {

		return "USRM " + formatter.format(LocalDateTime.now())
				+ (new SecureRandom().nextInt(RANDOM_UPPER) + RANDOM_LOWER);
	}

	private String generateLogs(HttpServletRequest request, HttpServletResponse response, Exception ex,
			String errorId) {

		StringBuilder result = new StringBuilder(
				ExceptionMessage.EXCEPTION.getValue() + "ERROR ID IS: " + errorId + " \n");
		result.append("### URL : (" + request.getMethod() + ") " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : "") + "\n");

		if (StringUtils.equalsAnyIgnoreCase(request.getMethod(), "POST", "PUT")) {
			try {
				result.append("### POST Data : "
						+ request.getReader().lines().collect(Collectors.joining(System.lineSeparator())) + "\n");
			} catch (IOException e1) {
			}
		}
		result.append("### Exception error : " + ExceptionUtils.getStackTrace(ex) + "\n");
		result.append(ExceptionMessage.EXCEPTION.getValue());
		log.error(result.toString());
		return errorId;
	}
    
  
}
