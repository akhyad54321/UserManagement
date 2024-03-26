package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MCIServiceGetByEUNResposne {

	
	@JsonProperty("cr_type")
	private String crType;
	
	@JsonProperty("cr_status")
	private String crStatus;
	
	@JsonProperty("cr_national_number")
	private String crNationalNumber;
	
	@JsonProperty("cr_number")
	private String crNumber;
	
	@JsonProperty("cr_type_id")
	private String crTypeId;
	
	@JsonProperty("cr_issue_hijri_date")
	private String crIssueHijriDate;
	
	@JsonProperty("cr_expiry_hijri_date")
	private String crExpiryHijriDate;
	
	@JsonProperty("cr_city_ar")
	private String crCityAr;
	
	@JsonProperty("cr_city_en")
	private String crCityEn;
	
	@JsonProperty("cr_city_code")
	private String crCityCode;
	
	@JsonProperty("cr_name_ar")
	private String crNameAr;
	
	@JsonProperty("cr_capital")
	private String crCapital;
	
	@JsonProperty("cr_capital_specified")
	private Boolean crCapitalSpecified;
	
	@JsonProperty("cr_legal_type_ar")
	private String crLegalTypeAr;
	
	@JsonProperty("cr_legal_type_en")
	private String crLegalTypeEn;
	
	@JsonProperty("cr_legal_type_id")
	private String crLegalTypeId;
	
	@JsonProperty("cr_status_id")
	private String crStatusId;
	
	@JsonProperty("cr_status_reason_ar")
	private String crStatusReasonAr;
	
	@JsonProperty("cr_status_reason_en")
	private String crStatusReasonEn;
	
	@JsonProperty("main_cr_number")
	private String mainCrNumber;
	
	@JsonProperty("cr_fax_no")
	private String crFaxNo;
	
	@JsonProperty("cr_address")
	private String crAddress;
	
	@JsonProperty("isic_activities_list")
	private List<Object> isicActivitiesList;
}
