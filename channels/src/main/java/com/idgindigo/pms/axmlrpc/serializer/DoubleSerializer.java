package com.idgindigo.pms.axmlrpc.serializer;

//import de.timroes.axmlrpc.XmlRpcException;
//import de.timroes.axmlrpc.XmlUtil;

import com.idgindigo.pms.axmlrpc.XmlRpcException;
import com.idgindigo.pms.axmlrpc.XmlUtil;
import com.idgindigo.pms.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

import java.math.BigDecimal;

/**
 * This serializer is responsible for floating point numbers.
 *
 * @author Tim Roes
 */
public class DoubleSerializer implements Serializer {

    public Object deserialize(Element content) throws XmlRpcException {
        return Double.valueOf(XmlUtil.getOnlyTextContent(content.getChildNodes()));
    }

    public XmlElement serialize(Object object) {
        // Turn double value of object into a BigDecimal to get the
        // right decimal point format.
        BigDecimal bd = BigDecimal.valueOf(((Number) object).doubleValue());
        return XmlUtil.makeXmlTag(SerializerHandler.TYPE_DOUBLE, bd.toPlainString());
    }

}
