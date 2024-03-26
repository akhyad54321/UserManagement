package sa.tabadul.useraccessmanagement.exception;

import org.springframework.http.HttpStatus;


public class BusinessException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer code = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public BusinessException(Integer code, String message) {

        super(message);
        this.code = code;
    }

    public BusinessException(String message) {

        super(message);

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
