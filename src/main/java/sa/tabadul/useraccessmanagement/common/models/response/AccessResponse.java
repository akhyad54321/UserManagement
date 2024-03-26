package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDateTime;

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
public class AccessResponse {

	@JsonProperty("head_id")
	private Integer headId;
	private String header;

	@JsonProperty("header_ar")
	private String headerAr;

	@JsonProperty("header_description")
	private String headerDescription;

	@JsonProperty("header_icon_path")
	private String headerIconPath;

	@JsonProperty("head_sequence_no")
	private Integer headSequenceNo;

	@JsonProperty("module_id")
	private Integer moduleId;

	private String module;

	@JsonProperty("module_description")
	private String moduleDescription;

	@JsonProperty("module_icon_path")
	private String moduleIconPath;

	@JsonProperty("module_route")
	private String moduleRoute;

	@JsonProperty("page_id")
	private Integer pageId;

	@JsonProperty("mawani_menu_setting")
	private String mawaniMenuSetting;

	private String page;

	@JsonProperty("page_arabic")
	private String pageArabic;

	@JsonProperty("page_code")
	private String pageCode;

	@JsonProperty("page_description")
	private String pageDescription;

	@JsonProperty("page_description_arabic")
	private String pageDescriptionArabic;

	@JsonProperty("page_icon_path")
	private String pageIconPath;

	@JsonProperty("page_route")
	private String pageRoute;

	@JsonProperty("page_sequence")
	private Integer pageSequence;

	@JsonProperty("stakeholder_category_RID")
	private Integer stakeholderCategoryRID;

	@JsonProperty("stakeholdertype_ID")
	private Integer stakeholdertypeID;

	@JsonProperty("app_id")
	private Integer appId;

	private Integer skateHolderID;

	@JsonProperty("accesspage_ID")
	private Integer accesspageID;

	@JsonProperty("access_view")
	private Boolean accessView;

	@JsonProperty("access_create")
	private Boolean accessCreate;

	@JsonProperty("access_edit")
	private Boolean accessEdit;

	@JsonProperty("access_delete")
	private Boolean accessDelete;

	@JsonProperty("access_print")
	private Boolean accessPrint;

	@JsonProperty("access_approve_reject")
	private Boolean accessApproveReject;

	@JsonProperty("created_by")
	private String createdBy;

	@JsonProperty("created_date")
	private LocalDateTime createdDate;

	@JsonProperty("updated_by")
	private String updatedBy;

	@JsonProperty("updated_date")
	private LocalDateTime updatedDate;

	@JsonProperty("is_active")
	private Boolean isActive;

	private String endpointUrl;

}
