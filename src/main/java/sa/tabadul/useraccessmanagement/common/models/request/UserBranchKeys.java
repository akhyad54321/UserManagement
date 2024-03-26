package sa.tabadul.useraccessmanagement.common.models.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBranchKeys {
	
	private String orgId;
	private String branchId;
	private String userId;
	private String stakeHolderType="-";
	private List<Integer> requestTypeRid = new ArrayList<>();
	private List<Integer> statusTypeRid = new ArrayList<>();
	private String appId;
	private String portRid;

}
