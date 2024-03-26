package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PortPermitList {
	
	private Integer id;
	private Long requestNo;
	private String orgName;
	private Integer portRid;
	private LocalDateTime requestDate;
	private String userId;
	private Integer approvalStatusRid;
	private String licenceNo;
	private LocalDateTime licenceInitiationDate;
	private LocalDate licenceExpiryDate;
	private Long eunn;
	private Integer requestTypeRid;
	private LocalDateTime updatedDate;

}
