package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "ROLE_MANAGEMENT", name = "ACCESS_PAGE")
public class AccessPage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(insertable=false, updatable=false, name = "access_module_ID")
	@JsonProperty("access_module_ID")
    private Integer accessModuleID;
	
	@JsonProperty("app_id")
	@Column(name = "app_id")
    private Integer appId;
    private String page;
    
    @JsonProperty("page_code")
	@Column(name = "page_code")
    private String pageCode;
    
    @JsonProperty("page_arabic")
	@Column(name = "page_arabic")
    private String pageArabic;
    
    @JsonProperty("page_description_arabic")
	@Column(name = "page_description_arabic")
    private String pageDescriptionArabic;
    
    @JsonProperty("page_description")
	@Column(name = "page_description")
    private String pageDescription;
    
    @JsonProperty("page_icon_path")
	@Column(name = "page_icon_path")
    private String pageIconPath;
    
    @JsonProperty("page_route")
	@Column(name = "page_route")
    private String pageRoute;
    
    @JsonProperty("mawani_menu_setting")
   	@Column(name = "mawani_menu_setting")
    private String mawaniMenuSetting;
    
    @JsonProperty("created_by")
	@Column(name = "created_by")
    private String createdBy;
    
    @JsonProperty("created_date")
	@Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @JsonProperty("updated_by")
	@Column(name = "updated_by")
    private String updatedBy;
    
    @JsonProperty("updated_date")
	@Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @JsonProperty("is_active")
	@Column(name = "is_active")
    private Boolean isActive;
    
    private Integer sequence; 

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "access_module_ID")
    @JsonBackReference
    private AccessModule accessPage;
    
    @OneToMany(mappedBy = "accessPageStakeHolder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AccessPageStakeHolder> accessPageStakeHolder = new ArrayList<>() ;
    
    
    


    
}
