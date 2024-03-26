package sa.tabadul.useraccessmanagement.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsTemplate {
	
	private Integer accesspageID;
    private Integer eventRID;
    private String mobile;
    private String smsEng;
    private String smsArb;
   
}
