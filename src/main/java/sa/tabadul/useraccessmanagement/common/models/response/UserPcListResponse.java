package sa.tabadul.useraccessmanagement.common.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPcListResponse {
	
	private String orgId;
	private String orgName;
	private Integer id;
	private String branchId;
	private String branchName;

}
