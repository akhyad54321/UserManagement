package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortPermitListResponse {

	
	private Long requestNo;
	private String orgName;
	private String portOfSubmission;
	private LocalDateTime requestDate;
	private String userId;
	private Integer approvalStatusRid;
	private String status;
	private String licenceNo;
	private LocalDate licenceInitiationDate;
	private LocalDate licenceExpiryDate;
	private Long eunn;
	private Integer requestTypeRid;
	private String requestTypeEnglish;
	private String requestTypeArabic;
	private Integer requestStatusRid;
	private String requestStatusEnglish;
	private String requestStatusArabic;
	private LocalDateTime updatedDate;
	private Boolean isActive;
	
}
