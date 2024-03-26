package sa.tabadul.useraccessmanagement.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attributes {

	private List<Boolean> canBeAssigned;
	private List<String> createdBy;
	private List<String> createdDate;
	private List<String> updatedBy;
	private List<String> updatedDate;
	private List<Boolean> isActive;
	private List<Boolean> isLicenceRequired;
	private List<String> roleDescriptionArabic;
	private List<String> roleDescriptionEnglish;
	private List<String> roleNameArabic;
	private List<String> roleNameEnglish;
	private List<String> rollType;
	private List<String> roleCode;
	private List<String> appId;
	private List<String> id;
	private List<String> portRid;

}
