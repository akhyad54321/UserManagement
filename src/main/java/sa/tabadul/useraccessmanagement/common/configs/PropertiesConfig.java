package sa.tabadul.useraccessmanagement.common.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class PropertiesConfig {

	@Value("${baseUrl}")
	private String baseUrl;

	@Value("${keyCloakUrl}")
	private String keyCloakUrl;

	@Value("${KeyCloakVerify}")
	private String KeyCloakVerify;

	@Value("${portById}")
	private String portById;

	@Value("${codeMasterByKeys}")
	private String codeMasterByKeys;

	@Value("${listOfPorts}")
	private String listOfPorts;

	@Value("${locationById}")
	private String locationById;

	@Value("${listofLocations}")
	private String listofLocations;

	@Value("${eunInfo}")
	private String eunInfo;

	@Value("${eunOwner}")
	private String eunOwner;

	@Value("${mciService}")
	private String mciSerivce;

	@Value("${authUsername}")
	private String username;

	@Value("${authPassword}")
	private String password;

	@Value("${keyCloakBaseUrl}")
	private String keyCloakBaseUrl;

	@Value("${keyCloakUserCreation}")
	private String keyCloakUserCreation;

	@Value("${keyCloakUserVerify}")
	private String keyCloakUserVerify;

	@Value("${KeyCloakBaseUrlPcs}")
	private String KeyCloakBaseUrlPcs;

	@Value("${KeyCloakResetPass}")
	private String KeyCloakResetPass;

	@Value("${KeyCloakDeactive}")
	private String KeyCloakDeactive;
		
	@Value("${redhatpmisurl}")
	private String redhatpmisurl;
	
	@Value("${keycloakpolicycreate}")
	private String policyCreate;
	
	@Value("${pmisClientId}")
	private String pmisClientId;
	
	@Value("${getAttributesUrl}")
	private String getAttributesUrl;
	
	@Value("${resourceServerId}")
	private String resourceServerId;
	
}
