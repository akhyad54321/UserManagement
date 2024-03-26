package sa.tabadul.useraccessmanagement.common.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBranchDetailsResponse {
	
	@JsonProperty("employee_name")
	private String userName;	
	
	private Integer id;
	
	@JsonProperty("user_id")
	private String userId;
	
	@JsonProperty("stakeholdertype_ID")
	private Integer stakeHolderTypeId;
	
	private String stakeHolderNameEnglish;
	
	private String stakeHolderNameArabic;
	
	private Integer stakeHolderSubRoleId;
	
	private String stakeHolderSubRoleNameEnglish;
	
	private String stakeHolderSubRoleNameArabic;
	

	@JsonProperty("national_ID")
	private String nationalId;
	
	@JsonProperty("unit_number")
	private String unitNumber;
	
	@JsonProperty("zip_code")
	private Integer zipCode;
	
	@JsonProperty("building_number")
	private String buildingNumber;
	
	private String street;
	
	private String district;
	
	private Integer city;
	
	private String cityDescEnglish;
	
	private String cityDescArabic;
	
	private String address;
	
	private String email;
	
	@JsonProperty("phone_no")
	private Long phoneNo;
	
	private String branchId;
	private String branchName;
	private Integer branchLocationRid;
	private String branchLocationNameEnglish;
	private String branchLocationNameArabic;
	private String repNo;
	private Integer portRid;
	private String portLocationEnglish;
	private String portLocationArabic;
	private String orgId;
	private String orgName;
	private String licenceNo;
	private String registrationTypeEng;
	private String registrationTypeArabic;
	private String registrationThroughEng;
	private String registrationThroughArabic;
	private String documentTypeEng;
	private String documentTypeArabic;
	private String orgNationalId;
	

}
