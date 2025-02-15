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
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="TIN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CertificateType" type="{http://yefi.gov.sa/DZIT/ZakatCertificate/xml/schemas/version/3.0}ZakatCertificateType"/&gt;
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
    "tin",
    "certificateType"
})
@XmlRootElement(name = "GetZakatCertificateByTINandCertificatesType")
public class GetZakatCertificateByTINandCertificatesType {

    @XmlElement(name = "TIN")
    protected String tin;
    @XmlElement(name = "CertificateType", required = true)
    @XmlSchemaType(name = "string")
    protected ZakatCertificateType certificateType;

    /**
     * Gets the value of the tin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIN() {
        return tin;
    }

    /**
     * Sets the value of the tin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIN(String value) {
        this.tin = value;
    }

    /**
     * Gets the value of the certificateType property.
     * 
     * @return
     *     possible object is
     *     {@link ZakatCertificateType }
     *     
     */
    public ZakatCertificateType getCertificateType() {
        return certificateType;
    }

    /**
     * Sets the value of the certificateType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZakatCertificateType }
     *     
     */
    public void setCertificateType(ZakatCertificateType value) {
        this.certificateType = value;
    }

}
