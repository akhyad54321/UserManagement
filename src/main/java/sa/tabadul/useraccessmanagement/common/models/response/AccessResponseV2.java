package sa.tabadul.useraccessmanagement.common.models.response;


import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class AccessResponseV2 {


	@JsonProperty("page_id")
	private String pageId = null;

	private String page = null;

	@JsonProperty("stakeholdertype_ID")
	private String stakeholdertypeID = null;

	@JsonProperty("app_id")
	private String appId = null;

	@JsonProperty("access_view")
	private Boolean accessView = false;
	
	@JsonProperty("access_view_id")
	private String accessViewId;

	@JsonProperty("access_create")
	private Boolean accessCreate = false;
	
	@JsonProperty("access_create_id")
	private String accessCreateId;

	@JsonProperty("access_edit")
	private Boolean accessEdit = false;
	
	@JsonProperty("access_edit_id")
	private String accessEditId;

	@JsonProperty("access_delete")
	private Boolean accessDelete = false;
	
	@JsonProperty("access_delete_id")
	private String accessDeleteId;

	@JsonProperty("access_print")
	private Boolean accessPrint = false;
	
	@JsonProperty("access_print_id")
	private String accessPrintId;

	@JsonProperty("access_approve_reject")
	private Boolean accessApproveReject = false;
	
	@JsonProperty("access_approve_reject_id")
	private String accessApproveRejectId;

	private String policyId = null;
	
	private String policyName = null;

	
}
