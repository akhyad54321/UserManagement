package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessGetResponseV2 {
	
	private String moduleName;
	private String stakeHolder;
	private List<AccessResponseV2> pages;

}
