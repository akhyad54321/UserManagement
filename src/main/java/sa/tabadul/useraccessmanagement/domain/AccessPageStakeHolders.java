package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(schema = "ROLE_MANAGEMENT" , name = "STAKEHOLDERTYPE_ACCESS")
public class AccessPageStakeHolders {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@JsonProperty("stakeholder_category_RID")
	@Column(name = "stakeholder_category_RID")
    private Integer stakeholderCategoryRID;
	
	
	@JsonProperty("stakeholdertype_ID")
	@Column(name = "stakeholdertype_ID")
    private Integer stakeholdertypeID;
    
	@JsonProperty("accesspage_ID")
    @Column(name = "accesspage_ID")
    private Integer accesspageID;
	
	@JsonProperty("access_view")
	@Column(name = "access_view")
    private Boolean accessView;
	
	@JsonProperty("access_create")
	@Column(name = "access_create")
    private Boolean accessCreate;
	
	@JsonProperty("access_edit")
	@Column(name = "access_edit")
    private Boolean accessEdit;
	
	@JsonProperty("access_delete")
	@Column(name = "access_delete")
    private Boolean accessDelete;
	
	@JsonProperty("access_print")
	@Column(name = "access_print")
    private Boolean accessPrint;
	
	@JsonProperty("access_approve_reject")
	@Column(name = "access_approve_reject")
    private Boolean accessApproveReject;
	
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
    
    @Column(name = "is_active")
    @JsonProperty("is_active")
    private Boolean isActive;
    
}
