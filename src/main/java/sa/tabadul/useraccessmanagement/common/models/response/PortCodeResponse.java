package sa.tabadul.useraccessmanagement.common.models.response;

import lombok.Data;
import sa.tabadul.useraccessmanagement.domain.PortMaster;

@Data
public class PortCodeResponse {
	private Integer statusCode;
    private String statusMessage;
    private PortMaster data;
	
  

}
