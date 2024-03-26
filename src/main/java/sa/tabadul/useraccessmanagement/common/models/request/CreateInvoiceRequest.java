package sa.tabadul.useraccessmanagement.common.models.request;

import javax.xml.bind.annotation.*;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@XmlRootElement(namespace = "http://ws.licenses.www.mawani.com/")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateInvoiceRequest {

	@XmlElement
	private String CRN;

	@XmlElement
	private String ORDER_NUMBER;

	@XmlElement
	private String ORDER_DATE;

	@XmlElement
	private String APPLICANT_NAME;

	@XmlElement
	private double INVOICE_AMOUNT;

	@XmlElement(name = "AGENT_TYEP")
	private int AGENT_TYPE;

	@XmlElement(name = "LIC_TYPE")
	private int LIC_TYPE;
}
