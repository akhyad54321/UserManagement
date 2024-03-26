package sa.tabadul.useraccessmanagement.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;



@Data
@Entity
@Table(name="MST_STAKEHOLDERTYPE" , schema = "ROLE_MANAGEMENT")
public class RoleMaster implements Serializable{
	
	private static final long serialVersionUID = -4871178958448224376L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Column(name="role_code",unique=true)
	private String roleCode;
	
	@NotNull
	@Column(name="role_name_english",unique=true)
	private String roleNameEnglish;
	
	@NotNull
	@Column(name="role_name_arabic",unique=true)
	private String roleNameArabic;
	
	@NotNull
	@Column(name="role_description_english",unique=true)
	private String roleDescriptionEnglish;
	
	@NotNull
	@Column(name="role_description_arabic",unique=true)
	private String roleDescriptionArabic;
	
	@Column(name="roll_type")
	private String rollType;
	
	@Column(name="is_licence_required")
	private Boolean isLicenceRequired;
	
	@Column(name="can_be_assigned")
	private Boolean canBeAssigned;
	
	@Column(name="app_id")
	private Integer appId;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="created_date")
	@JsonFormat(shape = Shape.STRING)
	private LocalDateTime createdDate;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	@Column(name="updated_date")
	@JsonFormat(shape = Shape.STRING)
	private LocalDateTime updatedDate;
	
	@Column(name="is_active")
	private Boolean isActive;
	
	
	@PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        //updatedDate = createdDate;
        isActive = true;
    }

	@Transient
	private String roleId;
	
	@Transient
	private String portRid;


}

