package sa.tabadul.useraccessmanagement.common.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleTypeResponse {
	
	private Integer id;
	
	@JsonProperty("role_code")
	private String roleCode;
	
	@JsonProperty("role_name_english")
	private String roleNameEnglish;
	
	@JsonProperty("role_name_arabic")
	private String roleNameArabic;
	
	@JsonProperty("role_description_english")
	private String roleDescriptionEnglish;
	
	@JsonProperty("role_description_arabic")
	private String roleDescriptionArabic;
	

}
