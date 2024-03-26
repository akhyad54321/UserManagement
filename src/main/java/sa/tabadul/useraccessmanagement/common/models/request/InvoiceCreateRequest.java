package sa.tabadul.useraccessmanagement.common.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceCreateRequest {
	
	private String crn;
    private String orderNumber;
    private String orderDate;
    private Integer invoiceAmount;
    private Integer agentType;
    private Integer licType;

}
