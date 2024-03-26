package sa.tabadul.useraccessmanagement.common.models.request;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import sa.tabadul.useraccessmanagement.common.models.response.CreateInvoiceReturn;

@XmlRootElement(name = "createInvoiceResponse", namespace = "http://ws.licenses.www.mawani.com/")
public class CreateInvoiceResponse {

    private CreateInvoiceReturn returnDetails;

    @XmlElement(name = "return")
    public CreateInvoiceReturn getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(CreateInvoiceReturn returnDetails) {
        this.returnDetails = returnDetails;
    }
}
