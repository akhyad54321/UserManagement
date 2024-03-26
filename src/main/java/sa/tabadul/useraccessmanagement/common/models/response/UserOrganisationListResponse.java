package sa.tabadul.useraccessmanagement.common.models.response;



import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;

@Getter
@Setter
public class UserOrganisationListResponse {
	
	private Long crn = null;
	private String userId = "-";
	
	@JsonFormat(pattern = CommonCodes.ORG_DATE)
	private LocalDateTime createdDate = null;
	@JsonFormat(pattern = CommonCodes.ORG_DATE)
	private LocalDateTime updatedDate = null;
	private String orgName = "-";
	private String roleNameEnglish = "-";
	private String roleNameArabic = "-";
	private String nationalId = "-";
	private String status = "-";
	private LocalDate fromDate = null;
	private LocalDate toDate = null;

}
