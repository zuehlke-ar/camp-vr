//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.22 at 11:32:38 PM CEST 
//


package net.opengis.kml;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

public class Scaleliteral
    extends JAXBElement<Double>
{

    protected final static QName NAME = new QName("http://www.opengis.net/kml/2.2", "scale");

    public Scaleliteral(Double value) {
        super(NAME, ((Class) Double.class), null, value);
    }

    public Scaleliteral() {
        super(NAME, ((Class) Double.class), null, null);
    }

}
