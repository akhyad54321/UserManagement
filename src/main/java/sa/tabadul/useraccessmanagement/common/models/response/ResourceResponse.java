package sa.tabadul.useraccessmanagement.common.models.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {
	
	private String ResourceID;
	private String ResourceName;
	private String PermissionID;
	private String PermissionName;
	private String PolicyID;
	private String PolicyName;
	
}
