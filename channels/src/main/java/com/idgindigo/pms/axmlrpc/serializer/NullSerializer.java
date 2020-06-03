package com.idgindigo.pms.axmlrpc.serializer;

import com.idgindigo.pms.axmlrpc.XmlRpcException;
import com.idgindigo.pms.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

/**
 * @author Tim Roes
 */
public class NullSerializer implements Serializer {

    @Override
    public Object deserialize(Element content) throws XmlRpcException {
        return null;
    }

    @Override
    public XmlElement serialize(Object object) {
        return new XmlElement(SerializerHandler.TYPE_NULL);
    }

}