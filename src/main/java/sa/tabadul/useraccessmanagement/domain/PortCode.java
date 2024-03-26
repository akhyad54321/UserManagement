/**
 * 
 */
package sa.tabadul.useraccessmanagement.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name ="[MastersLookup].[MASTER].[MST_PORT]" )
public class PortCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "port_code", length = 12, nullable = false)
	private String portCode;

	@Column(name = "port_name_english", length = 400, nullable = false)
	private String portNameEnglish;

	@Column(name = "port_name_arabic", length = 400, nullable = false)
	private String portNameArabic;

	@Column(name = "port_type_ID", nullable = false)
	private Integer portTypeId;

	@Column(name = "country_ID", nullable = false)
	private Integer countryId;

	@Column(name = "is_rail_service_port", nullable = false)
	private Boolean isRailServicePort;

	@Column(name = "port_UN_location", length = 50, nullable = false)
	private String portUNLocation;

	@Column(name = "time_zone", length = 50, nullable = false)
	private String timeZone;

	@Column(name = "created_by", length = 72, nullable = false)
	private String createdBy;

	@Column(name = "created_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "updated_by", length = 72, nullable = false)
	private String updatedBy;

	@Column(name = "updated_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	public PortCode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PortCode(String portCode, String portNameEnglish, String portNameArabic, Integer portTypeId,
			Integer countryId, Boolean isRailServicePort, String portUNLocation, String timeZone, String createdBy,
			Date createdDate, String updatedBy, Date updatedDate, Boolean isActive) {
		super();
		this.portCode = portCode;
		this.portNameEnglish = portNameEnglish;
		this.portNameArabic = portNameArabic;
		this.portTypeId = portTypeId;
		this.countryId = countryId;
		this.isRailServicePort = isRailServicePort;
		this.portUNLocation = portUNLocation;
		this.timeZone = timeZone;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.isActive = isActive;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getPortNameEnglish() {
		return portNameEnglish;
	}

	public void setPortNameEnglish(String portNameEnglish) {
		this.portNameEnglish = portNameEnglish;
	}

	public String getPortNameArabic() {
		return portNameArabic;
	}

	public void setPortNameArabic(String portNameArabic) {
		this.portNameArabic = portNameArabic;
	}

	public Integer getPortTypeId() {
		return portTypeId;
	}

	public void setPortTypeId(Integer portTypeId) {
		this.portTypeId = portTypeId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Boolean getIsRailServicePort() {
		return isRailServicePort;
	}

	public void setIsRailServicePort(Boolean isRailServicePort) {
		this.isRailServicePort = isRailServicePort;
	}

	public String getPortUNLocation() {
		return portUNLocation;
	}

	public void setPortUNLocation(String portUNLocation) {
		this.portUNLocation = portUNLocation;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}




}
