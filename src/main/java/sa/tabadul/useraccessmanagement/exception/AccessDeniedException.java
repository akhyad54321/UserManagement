package sa.tabadul.useraccessmanagement.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer code = HttpStatus.UNAUTHORIZED.value();

    public AccessDeniedException() {
        super("Sorry, You are un-authorized to access this resource");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
