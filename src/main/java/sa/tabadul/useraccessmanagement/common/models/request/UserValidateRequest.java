package sa.tabadul.useraccessmanagement.common.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserValidateRequest {
	
	@JsonProperty("user_id")
	private String userId;
	private String email;
	private String mobileNo;
	private Integer otp;
	
	

}
