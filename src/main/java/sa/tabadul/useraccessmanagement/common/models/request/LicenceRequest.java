package sa.tabadul.useraccessmanagement.common.models.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenceRequest {
	
	
	private Integer id;

	private Integer userOrgId;

	private Long crn;

	private Integer licenseTypeRid;

	private Integer investorTypeRid;
	
	private Integer requestTypeRid;

	private Long eunn;

	private String establishmentName;

	private String establishmentType;

	private String establishmentActivity;

	private String establishmentStatus;

	private LocalDate establishmentIssueDate;

	private LocalDate establishmentExpiryDate;

	private String establishmentCity;

	private String recordType;

	private String address;

	private String mailbox;

	private String managerName;

	private String managerNationality;

	private String listOfPartners;

	private Long sadadNo;

	private String licenseNumber;

	private LocalDate licenseExpiryDate;

	private Integer licenseStatusRid;

	private Integer approvalStatusRid;

	private String createdBy;

	private String updatedBy;


}
