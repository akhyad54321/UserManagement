package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KeyCloakResponse {
	
	private List<KeyCloakUserDetails> userDetails = new ArrayList<>();

}
