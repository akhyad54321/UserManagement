package sa.tabadul.useraccessmanagement.common.models.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

	private String userName;
	private Integer userType;
	private Long userMobile;
	private UserOrganizationRequest organization;
	private UserBranchRequest branch;
	private String portRid;
	private String portType;
	private List<String> roles = new ArrayList<>();
}
