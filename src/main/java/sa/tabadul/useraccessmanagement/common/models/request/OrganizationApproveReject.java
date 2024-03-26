package sa.tabadul.useraccessmanagement.common.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationApproveReject {
	
	private Long crn;
	private Integer approvalStatusRid;
	private String updatedBy;
	private String ActionById;
	private String remark;
	private Boolean isActive;

}
