package sa.tabadul.useraccessmanagement.common.models.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessPages {

	private Integer id;
	
	@JsonProperty("access_module_ID")
	private Integer accessModuleID;
	
	@JsonProperty("app_id")
	private Integer appId;
	
	private String page;
	
	@JsonProperty("page_code")
	private String pageCode;
	
	@JsonProperty("page_description")
	private String pageDescription;
	
	@JsonProperty("page_icon_path")
	private String pageIconPath;
	
	@JsonProperty("page_route")
	private String pageRoute;
	
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
	private Integer sequence;

}
