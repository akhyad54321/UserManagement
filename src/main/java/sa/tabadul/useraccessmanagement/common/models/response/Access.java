package sa.tabadul.useraccessmanagement.common.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Access {
	private Integer headerId;
	private String header;
	private String headerAr;
	private String headerDescription;
	private String headerIconPath;
	private Integer headSequenceNo;
	private Integer moduleId;
	private String module;
	private String moduleDescription;
	private String moduleIconPath;
	private String moduleRoute;
	private Integer pageId;
	private String page;
	private String pageCode;
	private String pageArabic;
	private String pageDescriptionArabic;
	private String pageDescription;
	private String pageIconPath;
	private String pageRoute;
	private String mawaniMenuSetting;
	private Integer appId;
	private Integer sequence;
	private Integer stakeholderId;
	private Integer stakeholderCategoryRID;
	private Integer stakeholderTypeID;
	private Boolean accessView;
	private Boolean accessCreate;
	private Boolean accessEdit;
	private Boolean accessDelete;
	private Boolean accessPrint;
	private Boolean accessApproveReject;
	private Boolean isActive;

}
