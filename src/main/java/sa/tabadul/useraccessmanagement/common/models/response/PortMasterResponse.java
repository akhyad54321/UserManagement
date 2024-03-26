package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.List;

import lombok.Data;
import sa.tabadul.useraccessmanagement.domain.PortMaster;


@Data
public class PortMasterResponse {
	private Integer statusCode;
    private String statusMessage;
    private List<PortMaster> data;
	
	
  

}
