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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IdentifierStructure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IdentifierStructure"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NationalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IqamaNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Code700" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentifierStructure", namespace = "http://yefi.gov.sa/DZIT/ZakatCertificate/xml/schemas/version/3.0", propOrder = {
    "nationalID",
    "iqamaNumber",
    "code700"
})
public class IdentifierStructure {

    @XmlElement(name = "NationalID")
    protected String nationalID;
    @XmlElement(name = "IqamaNumber")
    protected String iqamaNumber;
    @XmlElement(name = "Code700")
    protected String code700;

    /**
     * Gets the value of the nationalID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalID() {
        return nationalID;
    }

    /**
     * Sets the value of the nationalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalID(String value) {
        this.nationalID = value;
    }

    /**
     * Gets the value of the iqamaNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIqamaNumber() {
        return iqamaNumber;
    }

    /**
     * Sets the value of the iqamaNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIqamaNumber(String value) {
        this.iqamaNumber = value;
    }

    /**
     * Gets the value of the code700 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode700() {
        return code700;
    }

    /**
     * Sets the value of the code700 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode700(String value) {
        this.code700 = value;
    }

}
