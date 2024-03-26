package sa.tabadul.useraccessmanagement.common.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserListRequest {
	
	@JsonProperty("stakeholdertype_ID")
	private Integer stakeholdertypeID;
	
	@JsonProperty("branch_ID")
	private String branchID;
	
	@JsonProperty("org_ID")
	private String orgID;
	
	private Integer portId;

}
