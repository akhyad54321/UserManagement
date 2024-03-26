package sa.tabadul.useraccessmanagement.domain;


import lombok.Data;



@Data
public class PortMaster {

	private Integer id;
	private String portCode;
	private String portNameEnglish;
	private String portNameArabic;
	private Integer portTypeId;
	private Integer countryId;
	private Boolean isRailServicePort;
	private String portUNLocation;
	private String timeZone;
	private String createdBy;
	private String updatedBy;
	private Boolean isActive;
	

	
}
