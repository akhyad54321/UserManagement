package sa.tabadul.useraccessmanagement.common.models.response;

import lombok.Data;
import sa.tabadul.useraccessmanagement.domain.LocationMaster;

@Data
public class LocationMasterResponse {

	private Integer statusCode;
    private String statusMessage;
    private LocationMaster data;
	
}
