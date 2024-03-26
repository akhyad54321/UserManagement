package sa.tabadul.useraccessmanagement.common.models.request;


import javax.xml.bind.annotation.XmlElement;

public class GetStatusRequest {

    private String Sadad_no;

    @XmlElement(name = "Sadad_no")
    public String getSadad_no() {
        return Sadad_no;
    }

    public void setSadad_no(String sadad_no) {
        Sadad_no = sadad_no;
    }
}
