package sa.tabadul.useraccessmanagement.common.models.request;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class GetStatusBody {

    private GetStatusRequest getStatus;

    @XmlElement(name = "getStatus", namespace = "http://ws.licenses.www.mawani.com/")
    public GetStatusRequest getGetStatus() {
        return getStatus;
    }

    public void setGetStatus(GetStatusRequest getStatus) {
        this.getStatus = getStatus;
    }
}
