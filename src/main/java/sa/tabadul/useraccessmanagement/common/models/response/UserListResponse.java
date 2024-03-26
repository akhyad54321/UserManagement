package sa.tabadul.useraccessmanagement.common.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserListResponse {
	
	private Integer id;
	
	@JsonProperty("user_id")
	private String userId;
	
	@JsonProperty("user_type")
	private String userType;
	
	@JsonProperty("org_ID")
	private String orgID;
	
	@JsonProperty("branch_ID")
	private String branchID;
	
	private String email;
	
	@JsonProperty("user_name")
	private String userName;
	
	private String uuid;
	
	@JsonProperty("org_name")
	private String orgName ;
	
	@JsonProperty("branch_name")
	private String branchName ;
	
	private Integer portId;
	
	private String portNameEnglish;
	
	private String portNameArabic;

}
