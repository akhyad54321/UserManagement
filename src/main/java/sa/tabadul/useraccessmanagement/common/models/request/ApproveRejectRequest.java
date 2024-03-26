package sa.tabadul.useraccessmanagement.common.models.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApproveRejectRequest {
	
	private Long crn;
	private String remark;
	private Integer approvalStatusRid;
	private String createdBy;
	private Integer requestTypeRid;
	private Integer requestStatusRid;
	private String filePath;
	private String fileName;
	private LocalDateTime suspensionTo;

}
