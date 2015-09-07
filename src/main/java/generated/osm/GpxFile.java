//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.22 at 11:32:39 PM CEST 
//


package generated.osm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="user" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="timestamp" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lat" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="pending" default="false">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="true"/>
 *             &lt;enumeration value="false"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="public" default="false">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="true"/>
 *             &lt;enumeration value="false"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="lon" use="required" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "gpx_file")
public class GpxFile {

    @XmlAttribute(name = "user", required = true)
    protected String user;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "timestamp", required = true)
    protected String timestamp;
    @XmlAttribute(name = "lat", required = true)
    protected float lat;
    @XmlAttribute(name = "pending")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String pending;
    @XmlAttribute(name = "public")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String _public;
    @XmlAttribute(name = "lon", required = true)
    protected float lon;
    @XmlAttribute(name = "id", required = true)
    protected String id;

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestamp(String value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the lat property.
     * 
     */
    public float getLat() {
        return lat;
    }

    /**
     * Sets the value of the lat property.
     * 
     */
    public void setLat(float value) {
        this.lat = value;
    }

    /**
     * Gets the value of the pending property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPending() {
        if (pending == null) {
            return "false";
        } else {
            return pending;
        }
    }

    /**
     * Sets the value of the pending property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPending(String value) {
        this.pending = value;
    }

    /**
     * Gets the value of the public property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublic() {
        if (_public == null) {
            return "false";
        } else {
            return _public;
        }
    }

    /**
     * Sets the value of the public property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublic(String value) {
        this._public = value;
    }

    /**
     * Gets the value of the lon property.
     * 
     */
    public float getLon() {
        return lon;
    }

    /**
     * Sets the value of the lon property.
     * 
     */
    public void setLon(float value) {
        this.lon = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
