package sa.tabadul.useraccessmanagement.common.models.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class Body {

    private CreateInvoiceRequest createInvoice;

    @XmlElement(name = "createInvoice", namespace = "http://ws.licenses.www.mawani.com/")
    public CreateInvoiceRequest getCreateInvoice() {
        return createInvoice;
    }

    public void setCreateInvoice(CreateInvoiceRequest createInvoice) {
        this.createInvoice = createInvoice;
    }
    
 
}
