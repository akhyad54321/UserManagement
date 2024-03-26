package sa.tabadul.useraccessmanagement.common.helpers;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApprovalStatusCode {

	public String getStatusName(Integer id) {

		switch (id) {
		case 1:
			return "Submitted";
		case 2:
			return "Drafted";
		case 3:
			return "Approved";
		case 4:
			return "Rejected";
		case 5:
			return "Cancelled";
		case 6:
			return "Resubmitted";

		default:
			return "Unknown";
		}

	}
	
	

	

}
