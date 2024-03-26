package sa.tabadul.useraccessmanagement.common.models.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceServerRequest {
	
	@NotNull
	private String moduleName;
	
	@NotNull
	private String stakeholderType;
	
	@NotNull
	private String appId;

	private String subRole;
}
