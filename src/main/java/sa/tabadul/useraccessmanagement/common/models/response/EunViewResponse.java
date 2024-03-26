package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EunViewResponse {
	
	
	private Long eun;
	private String establishmentName;
	private String establishmentType;
	private String establishmentStatus;
	private String establishmentActivity;
	private LocalDate establishmentIssueDate;
	private LocalDate establishmentExpiryDate;
	private String establishmentCity;
	private String recordType;
	private String address;
	private String mailBox;
	private String managerName;
	private String managerNationality;
	private List<String> ownerDetails = new ArrayList<>();
	
	
}
