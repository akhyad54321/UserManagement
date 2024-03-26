package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.Setter;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.domain.LicenseApplicationApproval;

@Getter
@Setter
public class LicenseCrnViewResponse {
	
	private Integer id;
	private Long requestNumber;
	
	@JsonFormat(shape = Shape.STRING, pattern = CommonCodes.DDMMYYYY)
	private LocalDateTime requestDate;
	private String requestStatus;
	private Integer requestStatusRid;
	private String orgName;
	private String licenseStatus;
	private Integer licenseStatusRid;
	private String requestType;
	private Integer requestTypeRid;
	@JsonFormat(shape = Shape.STRING, pattern = CommonCodes.DDMMYYYY)
	private LocalDateTime licenseUpdate;
	private String licenseNo;
	private String sadadNo;
	private LocalDate expiryDate;
	private Long validateForDays;
	private String licenseType;
	private Integer licenseTypeRid;
	private String investorType;
	private Integer invenstorTyperRid;
	private String iqmaNo;
	private Long eunn;
	private String establishmentName;
	private String listOfPartners;
	private String establishmentType;
	private String establishmentActivity;
	private String establishmentStatus;
	private LocalDate establishmentIssueDate;
	private LocalDate establishmentExpiryDate;
	private String establishmentCity;
	private String recordType;
	private String address;
	private String mailBox;
	private String managerName;
	private String managerNationality;
	private String fileName;
	private String filePath;
	private LocalDateTime suspensionTo;
	private List<String> ownerDetails = new ArrayList<>();
	private List<LicenseApplicationApproval> approval= new ArrayList<>();
	

}
