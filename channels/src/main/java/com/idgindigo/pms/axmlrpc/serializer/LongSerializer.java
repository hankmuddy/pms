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
class LongSerializer implements Serializer {

    public Object deserialize(Element content) throws XmlRpcException {
        return Long.parseLong(XmlUtil.getOnlyTextContent(content.getChildNodes()));
    }

    public XmlElement serialize(Object object) {
        return XmlUtil.makeXmlTag(SerializerHandler.TYPE_LONG,
                ((Long) object).toString());
    }

}
