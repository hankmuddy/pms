package com.idgindigo.pms.axmlrpc.serializer;

import com.idgindigo.pms.axmlrpc.XmlRpcException;
import com.idgindigo.pms.axmlrpc.XmlUtil;
import com.idgindigo.pms.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

/**
 * @author vomel
 * @since 31.01.14 18:49
 */
public class EmptyStringSerializer implements Serializer {

    @Override
    public Object deserialize(Element content) throws XmlRpcException {
        return null;
    }

    @Override
    public XmlElement serialize(Object object) {
        return XmlUtil.makeXmlTag(SerializerHandler.TYPE_STRING, "");
    }
}
