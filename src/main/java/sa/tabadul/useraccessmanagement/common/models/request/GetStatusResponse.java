package sa.tabadul.useraccessmanagement.common.models.request;


import javax.xml.bind.annotation.XmlElement;

import sa.tabadul.useraccessmanagement.common.models.response.ReturnDetails;

public class GetStatusResponse {

    private ReturnDetails returnDetails;

    @XmlElement(name = "return")
    public ReturnDetails getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(ReturnDetails returnDetails) {
        this.returnDetails = returnDetails;
    }
}
