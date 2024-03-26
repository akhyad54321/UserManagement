package sa.tabadul.useraccessmanagement.common.models.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccessRequest {

	

	@NotNull
	@JsonProperty("module_id")
	private Integer moduleId;
	
	@JsonProperty("sub_role")
	private Integer subRole;
	
	@NotNull
	@JsonProperty("stakeholder_type")
	private Integer stakeholderType;
	
	@NotNull
	@JsonProperty("app_id")
	private Integer appId;
	

}
