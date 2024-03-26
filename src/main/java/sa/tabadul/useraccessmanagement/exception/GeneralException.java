package sa.tabadul.useraccessmanagement.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GeneralException extends RuntimeException {
	
    
    
    private static final long serialVersionUID = 1L;
	Integer code = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public GeneralException(Integer code, String message) {

        super(message);
        this.code = code;
    }

    public GeneralException(String message) {

        super(message);

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    
    
}
