package sa.tabadul.useraccessmanagement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ExceptionMessage
 */

@Getter
@AllArgsConstructor
public enum ExceptionMessage {

    CRN_NOT_FOUND("Common Reference Number(CRN) not found "),
	
    USER_ID_NOT_FOUND("UserId Not Exists : "),
    
    MOB_NO_NOT_FOUND("Mobile No. Not Exists : "),
    
    EMAIL_NOT_FOUND("Email Not Exists : "),
    
    EMAIL_EXISTS("Email Already Exists : "),
    
    DATA_NOT_FOUND("data not found : "),
	
	STACKTRACE("STACKTRACE : "),
	
	SOMETHING_WENT_WRONG("Something Went Wrong"),
	
	FEILDS_EMPTY("All fields can't be blank"),
	
	DATA_ALREADY_EXISTS("data already exists "),
	
	EXCEPTION("---------------------EXCEPTION--------------"),
	
	VALIDATION_FAILED("Validation Failed"),
	
	USR_EXISTS("User Already Exists : "),
	
	MOB_EXISTS("Mobile No. Already Exists : "),
	
	DATE_DELETED("Data deleted successfully"),
	
	ROLE_EXISTS("Role Already Exists : "),
	
	INVALID_USER_TYPE("Invalid userType."),
	
	UNKOWN("Unknown"),
	
	ORGANIZATION_NOT_FOUND("Organization not found"),
	
	LIC_NOT_FOUND("License not found"),
	
	BRANCH_DETAILS_NOT_FOUND("Branch deatils not found with id "),
	
	ERROR_REFRESHING_CACHE("Error refreshing cache:"),
	
	ERROR_ACCESSING_CACHE("Error accessing cache : ");

    private final String value;

}