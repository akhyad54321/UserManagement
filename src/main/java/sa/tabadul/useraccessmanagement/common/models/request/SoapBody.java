package sa.tabadul.useraccessmanagement.common.models.request;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapBody {

    private CreateInvoiceResponse createInvoiceResponse;

    @XmlElement(name = "createInvoiceResponse", namespace = "http://ws.licenses.www.mawani.com/")
    public CreateInvoiceResponse getCreateInvoiceResponse() {
        return createInvoiceResponse;
    }

    public void setCreateInvoiceResponse(CreateInvoiceResponse createInvoiceResponse) {
        this.createInvoiceResponse = createInvoiceResponse;
    }
    
    private GetStatusResponse getStatusResponse;

    @XmlElement(name = "getStatusResponse", namespace = "http://ws.licenses.www.mawani.com/")
    public GetStatusResponse getGetStatusResponse() {
        return getStatusResponse;
    }

    public void setGetStatusResponse(GetStatusResponse getStatusResponse) {
        this.getStatusResponse = getStatusResponse;
    }
    
}
