package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBranchViewResponse {
	
	private String userName = "-";
	private Long phoneNumber = null;
	private String email = "-";
	private String branchId = "-";
	private String branchName = "-";
	private String branchLocation = "-";
	private String status;
	private String userType;
	private LocalDateTime createdDate = null;
	private LocalDateTime updatedDate = null;
	private Integer id = null;
	private String userId = "-";

}
