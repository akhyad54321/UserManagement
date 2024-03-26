package sa.tabadul.useraccessmanagement.common.models.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;

@Getter
@Setter
public class LicenseFilter {
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Integer stakeHolderTypeRid;
	private Integer approvalStatusRid;
	private Integer licenseStatusRid;
	private Integer requestTypeRid;
	
	
	public LocalDateTime getFromDate() {
		
		if(fromDate!=null)
		{
			LocalTime midnight = LocalTime.MIDNIGHT;
			return LocalDateTime.of(fromDate, midnight);
		}
		else
		{
			return null;
		}
	}
	
	
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDateTime getToDate() {
		
		if(toDate!=null)
		{
			LocalTime midnight = LocalTime.MIDNIGHT;
			return LocalDateTime.of(toDate, midnight);
		}
		else
		{
			return null;
		}
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	
	
	
	

}
