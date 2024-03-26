package sa.tabadul.useraccessmanagement.common.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredential {

	public Boolean temporary;
	public String type;
	public String value;
	
}
