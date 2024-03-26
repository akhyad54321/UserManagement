package sa.tabadul.useraccessmanagement.common.models.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPortPermitFilter {
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private Integer stakeHolderTypeRid;
	private Integer approvalStatusRid;
	private Integer requestStatusRid;
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
