package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDate;
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
@Table(schema = "[USER]", name = "[USER_REGISTRATION]")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @JsonProperty("app_id")
    @Column(name = "app_id")
    private Integer appId;
    


    @JsonProperty("employee_code")
    @Column(name = "employee_code")
    private String employeeCode;
    
    @JsonProperty("employee_name")
    @Column(name = "employee_name")
    private String employeeName;
    
    @JsonProperty("joining_date")
    @Column(name = "joining_date")
    private LocalDate joiningDate;
    
    @JsonProperty("department_RID")
    @Column(name = "department_RID")
    private Integer departmentRID;

    @Transient
    @JsonProperty("deparment_code")
    private String deparmentCode;

    @Transient
    @JsonProperty("deparment_code_desc_eng")
    private String deparmentCodeDescEng;

    @Transient
    @JsonProperty("deparment_code_desc_arabic")
    private String deparmentCodeDescArabic;

    @JsonProperty("designation_RID")
    @Column(name = "designation_RID")
    private Integer designationRID;

    @Transient
    @JsonProperty("designation_code")
    private String designationCode;

    @Transient
    @JsonProperty("designation_code_desc_eng")
    private String designationCodeDescEng;

    @Transient
    @JsonProperty("designation_code_desc_arabic")
    private String designationCodeDescArabic;

    @JsonProperty("port_RID")
    @Column(name = "port_RID")
    private Integer portRID;
    
    @JsonProperty("default_group_type_RID")
    @Column(name = "default_group_type_RID")
    private Integer defaultGroupTypeRID;

    @Transient
    @JsonProperty("default_group_code")
    private String defaultGroupCode;

    @Transient
    @JsonProperty("default_group_code_desc_eng")
    private String defaultGroupCodeDescEng;

    @Transient
    @JsonProperty("default_group_code_desc_arabic")
    private String defaultGroupCodeDescArabic;


    @JsonProperty("stakeholdertype_ID")
    @Column(name = "stakeholdertype_ID")
    private Integer stakeholdertypeID;
 

    @Transient
    @JsonProperty("roll_type")
    @Column(name = "roll_type")
    private String rollType;

    @Transient
    @JsonProperty("first_name")
    private String firstName;

    @Transient
    @JsonProperty("last_name")
    private String lastName;
    
    @Transient
    @JsonProperty("stakeholder_code")
    private String stakeholderCode;

    @Transient
    @JsonProperty("stakeholder_name_eng")
    private String stakeholderNameEng;
    
    @Transient
    @JsonProperty("stakeholder_name_arabic")
    private String stakeholderNameArabic;

    @Transient
    @JsonProperty("stakeholder_des_eng")
    private String stakeholderDesEng;

    @Transient
    @JsonProperty("stakeholder_des_arabic")
    private String stakeholderDesArabic;
    
    @JsonProperty("stakeholdertype_subrole_ID")
    @Column(name = "stakeholdertype_subrole_ID")
    private Integer stakeholdertypeSubroleID;
    
    @Transient
    @JsonProperty("stakeholdertype_subrole_code")
    private String stakeholdertypeSubroleCode;
    
    @Transient
    @JsonProperty("stakeholdertype_subrole_name_eng")
    private String stakeholdertypeSubroleNameEng;
    
    @Transient
    @JsonProperty("stakeholdertype_subrole_name_arabic")
    private String stakeholdertypeSubroleNameArabic;
    
    @Transient
    @JsonProperty("stakeholdertype_subrole_des_eng")
    private String stakeholdertypeSubroleDesEng;
    
    @Transient
    @JsonProperty("stakeholdertype_subrole_des_arabic")
    private String stakeholdertypeSubroleDesArabic;

    @Transient
    @JsonProperty("port_name_eng")
    private String portNameEng;
    
    @Transient
    @JsonProperty("port_name_arabic")
    private String portNameArabic;
    
    @Transient
    @JsonProperty("roleId")
    private Integer roleId;

    @Transient
    @JsonProperty("port_code")
    private String portCode;

    @JsonProperty("email")
    @Column(name = "email")
    private String email;
    
    @JsonProperty("phone_no")
    @Column(name = "phone_no")
    private Long phoneNo;

    @Column(name = "IAM_user_ID")
    @JsonProperty(value = "iam_user_ID")
    private String iamUserID;

    @JsonProperty("created_by")
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    @JsonProperty(value = "created_date")
    private LocalDateTime createdDate;
    
    @Transient
    @JsonProperty("updated_by")
    @Column(name = "updated_by")
    private String updatedBy;
    
    @JsonProperty("updated_date")
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "is_active")
    @JsonProperty(value = "is_active")
    private Boolean isActive;

    @JsonProperty("org_ID")
    @Column(name = "org_ID")
    private String orgID;
    
    @JsonProperty("branch_ID")
    @Column(name = "branch_ID")
    private String branchID;
    
    @JsonProperty("user_type")
    @Column(name = "user_type")
    private String userType;
    
    @JsonProperty("user_id")
    @Column(name = "user_id")
    private String userId;
    
    @JsonProperty("profile_image_path")
    @Column(name = "profile_image_path")
    private String profileImagePath;
    
    @Transient
    @JsonProperty("custom_no")
    private String customNo;
    
    @Transient
    @JsonProperty("licence_no")
    private String licenceNo;
    
    
    @JsonProperty("port_user_no")
    @Column(name = "port_user_no")
    private String portUserNo;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        isActive = true;
    }


}
