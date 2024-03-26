package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnersListResponse {
	
	
	@JsonProperty("cr_owners_list")
	private List<OwnerDetailsResponse> crOwnersList = new ArrayList<>();
	
	@JsonProperty("cr_national_number")
 	private String crNationalNumber;
	
	@JsonProperty("cr_number")
	private String crNumber;

}
