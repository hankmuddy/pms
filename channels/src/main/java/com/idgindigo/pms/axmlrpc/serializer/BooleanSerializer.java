package com.idgindigo.pms.axmlrpc.serializer;

//import de.timroes.axmlrpc.XmlRpcException;
//import de.timroes.axmlrpc.XmlUtil;

import com.idgindigo.pms.axmlrpc.XmlRpcException;
import com.idgindigo.pms.axmlrpc.XmlUtil;
import com.idgindigo.pms.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

/**
 * @author Tim Roes
 */
public class BooleanSerializer implements Serializer {

    public Object deserialize(Element content) throws XmlRpcException {
        return (XmlUtil.getOnlyTextContent(content.getChildNodes()).equals("1"))
                ? Boolean.TRUE : Boolean.FALSE;
    }

    public XmlElement serialize(Object object) {
        return XmlUtil.makeXmlTag(SerializerHandler.TYPE_BOOLEAN,
                ((Boolean) object == true) ? "1" : "0");
    }

}