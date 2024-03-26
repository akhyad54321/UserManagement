package sa.tabadul.useraccessmanagement.common.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PolicyRole {
	
	private String id;
	private Boolean required;

}
