package sa.tabadul.useraccessmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBranchRegistration {
	
	private UserBranch branchDetails;
	private Users userDetails;

}
