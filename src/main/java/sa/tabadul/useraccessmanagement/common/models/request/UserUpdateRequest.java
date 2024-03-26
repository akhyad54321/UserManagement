package sa.tabadul.useraccessmanagement.common.models.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import sa.tabadul.useraccessmanagement.domain.RoleMapping;


@Data
public class UserUpdateRequest {

	@NotNull
	@JsonProperty("module_id")
	private Integer moduleId;
	
	@NotNull
	@JsonProperty("stakeholder_type")
	private Integer stakeholderType;
	
	@NotNull
	@JsonProperty("app_id")
	private Integer appId;
	
	@JsonProperty("sub_role")
	private Integer subRole;
	
	private List<RoleMapping> activityList;
	
	
}
