package sa.tabadul.useraccessmanagement.common.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourcePermissionsPolicy {
	
	private String resourceId;
	private String resourceName;
	private String permissionId;
	private String permissionName;
	private String policyId;
	private String policyName;

}
