package sa.tabadul.useraccessmanagement.common.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerDetailsResponse {
	
	@JsonProperty("owner_name")
	private String ownerName;
	
	@JsonProperty("owner_nationality_ar")
	private String ownerNationalityAr;
	
	@JsonProperty("owner_nationality_en")
	private String ownerNationalityEn;
	
	@JsonProperty("owner_nationality_id")
	private String ownerNationalityId;
	
	@JsonProperty("relation_type")
	private String relationType;
	
	@JsonProperty("relation_type_id")
	private String relationTypeId;
	
	@JsonProperty("owner_id")
	private Object ownerId;

}
