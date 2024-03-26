package sa.tabadul.useraccessmanagement.common.models.request;

import lombok.Data;

@Data
public class UserOrganizationRequest {

	private String orgId;
	private String orgName;
	private String arabicOrgName;
	private String crn;
	private String emailId;
	private Long mobileNumber;
	private String address;
	private Long zipCode;
	private String licenceNo;
}
