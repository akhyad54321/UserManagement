package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import sa.tabadul.useraccessmanagement.domain.Users;


@Data
public class UserAccessResponse {
	
	private Users userdata ;
	private List<AccessResponse> useraccess = new ArrayList<>();

}
