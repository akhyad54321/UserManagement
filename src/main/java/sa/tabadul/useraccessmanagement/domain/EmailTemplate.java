package sa.tabadul.useraccessmanagement.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplate {
	
	private Integer accesspageID;
    private Integer eventRID;
    private String email;
    private String EmailHeaderEng;
    private String EmailHeaderArb;
    private String EmailBodyEng;
    private String EmailBodyArb;

}
