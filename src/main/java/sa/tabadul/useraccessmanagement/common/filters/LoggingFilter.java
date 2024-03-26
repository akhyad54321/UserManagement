package sa.tabadul.useraccessmanagement.common.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import sa.tabadul.useraccessmanagement.common.constants.LoggingMessages;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(LoggingFilter.class);


    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return "";
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);
        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());

        StringBuilder requestParams = new StringBuilder();
        request.getParameterMap().forEach((name, values) -> {
            for (String value : values) {
                if (requestParams.length() > 0) {
                    requestParams.append("&");
                }
                requestParams.append(name).append("=").append(value);
            }
        });
        String logMessage = String.format("%s | %s=%s | %s=%s | %s=%s | %s=%s | %s=%s | %s=%s | %s |",
                LoggingMessages.LOG_SEPERATOR,
                LoggingMessages.LOG_REQUEST_TYPE, request.getMethod(),
                LoggingMessages.LOG_URL, request.getRequestURI(),
                LoggingMessages.REQUEST_PARAMS, requestParams,
                LoggingMessages.LOG_REQUEST_BODY, requestBody.replaceAll("\r\n", ""),
                LoggingMessages.LOG_RESPONSE_STATUS, response.getStatus(),
                LoggingMessages.LOG_RESPONSE_BODY, responseBody,
                LoggingMessages.LOG_SEPERATOR);

        logger.info(logMessage);
        responseWrapper.copyBodyToResponse();
    }

}
