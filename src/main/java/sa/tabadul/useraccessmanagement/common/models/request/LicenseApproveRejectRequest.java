package sa.tabadul.useraccessmanagement.common.models.request;

import java.time.LocalDateTime;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseApproveRejectRequest {
	
	private Long crn;
	private String remark;
	private Integer approvalStatusRid;
	private Integer requestTypeRid;
	private Integer licenseStatusRid;
	private String createdBy;
	private String fileName;
	private String filePath;
	private LocalDateTime suspensionTo;
	
}
