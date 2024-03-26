package sa.tabadul.useraccessmanagement.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	
	@NotNull
	@Column(name="LOCATION_CODE",unique = true)
	private String locationCode;
	
	@NotNull
	@Column(name="LOCATION_DESCRIPTION_ENGLISH",unique=true)
	private String locationDescriptionEnglish;
	
	@NotNull
	@Column(name="LOCATION_DESCRIPTION_ARABIC",unique=true)
	private String locationDescriptionArabic;
	
	@NotNull
	@Column(name="LOCATION_TYPE")
	private String locationType;
	
	@Column(name="IS_ACTIVE")
	private Boolean is_active;
	
	@Column(name="CREATED_BY")
	private String createdBy;
		
	@Column(name="UPDATED_BY")
	private String updatedBy;
		
	@Column(name="PARENT_ID")
	private Integer parentId;
	
	@Column(name="IMAGE_PATH")
	private String imagePath;
	

}
