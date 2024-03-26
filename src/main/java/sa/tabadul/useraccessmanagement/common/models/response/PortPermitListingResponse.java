package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortPermitListingResponse {
	
	private Long crn;
	private String orgName;
	private LocalDateTime createdDate;
	private String userId;
	private Integer approvalStatusRid;
	private String licenceNo;
	private LocalDateTime licenceInitiationDate;
	private LocalDate licenceExpiryDate;
	private Long eunn;
	private Integer requestTypeRid;
	private Integer requestStatusRid;
	private LocalDateTime updatedDate;
	private Integer portRid;
	
}
