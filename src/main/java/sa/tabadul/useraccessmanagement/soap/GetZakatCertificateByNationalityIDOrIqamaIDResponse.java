//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.12.05 at 09:06:28 AM AST 
//


package sa.tabadul.useraccessmanagement.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetZakatCertificateByNationalityIDOrIqamaIDResult" type="{http://yesser.gov.sa/DZIT/ZakatCertificateService/version/3.0}getZakatCertificateByNationalityIDOrIqamaIDResponseObject" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getZakatCertificateByNationalityIDOrIqamaIDResult"
})
@XmlRootElement(name = "GetZakatCertificateByNationalityIDOrIqamaIDResponse")
public class GetZakatCertificateByNationalityIDOrIqamaIDResponse {

    @XmlElement(name = "GetZakatCertificateByNationalityIDOrIqamaIDResult")
    protected GetZakatCertificateByNationalityIDOrIqamaIDResponseObject getZakatCertificateByNationalityIDOrIqamaIDResult;

    /**
     * Gets the value of the getZakatCertificateByNationalityIDOrIqamaIDResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetZakatCertificateByNationalityIDOrIqamaIDResponseObject }
     *     
     */
    public GetZakatCertificateByNationalityIDOrIqamaIDResponseObject getGetZakatCertificateByNationalityIDOrIqamaIDResult() {
        return getZakatCertificateByNationalityIDOrIqamaIDResult;
    }

    /**
     * Sets the value of the getZakatCertificateByNationalityIDOrIqamaIDResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetZakatCertificateByNationalityIDOrIqamaIDResponseObject }
     *     
     */
    public void setGetZakatCertificateByNationalityIDOrIqamaIDResult(GetZakatCertificateByNationalityIDOrIqamaIDResponseObject value) {
        this.getZakatCertificateByNationalityIDOrIqamaIDResult = value;
    }

}
