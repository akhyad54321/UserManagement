package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.List;

import lombok.Data;
import sa.tabadul.useraccessmanagement.domain.LocationMaster;

@Data
public class LocationListResponse {
	
	private Integer statusCode;
    private String statusMessage;
    private List<LocationMaster> data;

}
