package sa.tabadul.useraccessmanagement.common.models.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationUserCreationResponse {
	
	private String iAmUserID;
	private Long crn;
	private Boolean isUserCreated;
	private String status;
	

}
