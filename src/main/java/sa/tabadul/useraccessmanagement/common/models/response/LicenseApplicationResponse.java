package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseApplicationResponse {
	
	private Long crn;
	private String createdBy;
	private String updatedBy;
	private LocalDateTime createdDate;
	

}
