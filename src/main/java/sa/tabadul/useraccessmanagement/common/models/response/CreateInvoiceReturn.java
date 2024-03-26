package sa.tabadul.useraccessmanagement.common.models.response;

 
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "return", namespace = "http://ws.licenses.www.mawani.com/")
public class CreateInvoiceReturn {

    private String sadadNo;
    private int statusCode;
    private String statusDetail;

    @XmlElement
    public String getSadadNo() {
        return sadadNo;
    }

    public void setSadadNo(String sadadNo) {
        this.sadadNo = sadadNo;
    }

    @XmlElement
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @XmlElement
    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }
}
