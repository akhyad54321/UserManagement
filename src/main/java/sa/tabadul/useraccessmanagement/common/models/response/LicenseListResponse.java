package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;

@Getter
@Setter
public class LicenseListResponse {

	private Long requestNo;
	private String orgName;
	private Long establishmentNumber;
	private String stakholderCategory;
	private String requestType;
	private Integer requestTypeRid;
	private Long licenseValidForDays;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonCodes.DDMMYYYY)
	private LocalDate expiryDate;
	private String userId;
	private String licenseNo;
	private String licenseStatus;
	private Integer licenseStatusRid;
	private String status;
	private LocalDateTime updatedDate;
	private Integer approvalStatusRid;
	private Boolean isActive;
}
