package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(schema = "[USER]", name = "[USER_ROLE]")
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_registration_id", insertable = false, updatable = false)
	@JsonProperty(value = "user_registration_id")
	private Integer userRegistrationId;

	@NotNull
	@Column(name = "stakeholder_type_id")
	@JsonProperty(value = "stakeholdertype_ID")
	private Integer stakeholderTypeId;

	
	@Column(name = "stakeholdertype_subrole_id")
	@JsonProperty(value = "stakeholdertype_subrole_ID")
	private Integer stakeholderSubroleId=0;

	@Column(name = "created_by")
	@JsonProperty(value = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	@JsonProperty(value = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "updated_by")
	@JsonProperty(value = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	@JsonProperty(value = "updated_date")
	private LocalDateTime updatedDate = LocalDateTime.now();

	@Column(name = "is_active")
	@JsonProperty(value = "is_active")
	private Boolean isActive;

	@PrePersist
	public void prePersist() {
		createdDate = LocalDateTime.now();
		isActive = true;
	}
	
    @Transient
    @JsonProperty(value = "stakeholder_code")
    private String stakeholderCode;
    
    @Transient
    @JsonProperty(value = "stakeholder_name_eng")
    private String stakeholderNameEng;
    
    @Transient
    @JsonProperty(value = "stakeholder_name_arabic")
    private String stakeholderNameArabic;
    
    @Transient
    @JsonProperty(value = "stakeholder_des_eng")
    private String stakeholderDesEng;

    @Transient
    @JsonProperty(value = "stakeholder_des_arabic")
    private String stakeholderDesArabic;
    
    
    @Transient
    @JsonProperty(value = "stakeholdertype_subrole_code")
    private String stakeholdertypeSubroleCode;
    
    @Transient
    @JsonProperty(value = "stakeholdertype_subrole_name_eng")
    private String stakeholdertypeSubroleNameEng;
    
    @Transient
    @JsonProperty(value = "stakeholdertype_subrole_name_arabic")
    private String stakeholdertypeSubroleNameArabic;
    
    @Transient
    @JsonProperty(value = "stakeholdertype_subrole_des_eng")
    private String stakeholdertypeSubroleDesEng;
    
    @Transient
    @JsonProperty(value = "stakeholdertype_subrole_des_arabic")
    private String stakeholdertypeSubroleDesArabic;

	@ManyToOne
	@JoinColumn(name = "user_registration_id", referencedColumnName = "id")
	@JsonBackReference
	private Users users;

}
