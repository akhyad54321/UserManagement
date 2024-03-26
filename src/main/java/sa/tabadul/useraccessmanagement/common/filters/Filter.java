package sa.tabadul.useraccessmanagement.common.filters;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
	
	private Boolean status;
	private String email;
	private String department;
	
	@JsonProperty("employee_name")
	private String employeeName;
	
	@JsonProperty("port_name")
	private String portName;
	
	@JsonProperty("stakeholder_type")
	private String stakeholderType;
	
	

}
