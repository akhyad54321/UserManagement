package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortPermitResponseDTO {

	  private Long crn;
	    private LocalDateTime createdDate;
	    private String createdBy;

}
