package sa.tabadul.useraccessmanagement.common.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
	
	public Boolean temporary;
	public String type;
	public String value;
	public String iamUserId;
	public Integer appId;

}
