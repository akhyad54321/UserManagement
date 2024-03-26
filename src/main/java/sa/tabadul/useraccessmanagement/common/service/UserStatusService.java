package sa.tabadul.useraccessmanagement.common.service;

import org.springframework.stereotype.Service;

@Service
public class UserStatusService {
	
	public String getStatusName(Boolean id) {

		if (id.equals(true)) {
			return "Active";
		} else {
			return "Deactive";
		} 
	}

}
