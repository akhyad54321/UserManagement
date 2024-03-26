package sa.tabadul.useraccessmanagement.common.models.request;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class GetStatusEnvelope {

    private Header header;
    private GetStatusBody body;

    @XmlElement(name = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
    public GetStatusBody getBody() {
        return body;
    }

    public void setBody(GetStatusBody body) {
        this.body = body;
    }
}
