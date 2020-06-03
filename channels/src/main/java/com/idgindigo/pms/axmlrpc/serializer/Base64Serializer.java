package com.idgindigo.pms.axmlrpc.serializer;

//import de.timroes.axmlrpc.XmlRpcException;
//import de.timroes.axmlrpc.XmlUtil;

import com.idgindigo.pms.axmlrpc.XmlRpcException;
import com.idgindigo.pms.axmlrpc.XmlUtil;
import com.idgindigo.pms.axmlrpc.base64.Base64;
import com.idgindigo.pms.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

//import de.timroes.base64.Base64;

/**
 * @author Tim Roes
 */
public class Base64Serializer implements Serializer {

    public Object deserialize(Element content) throws XmlRpcException {
        return Base64.decode(XmlUtil.getOnlyTextContent(content.getChildNodes()));
    }

    public XmlElement serialize(Object object) {
        return XmlUtil.makeXmlTag(SerializerHandler.TYPE_BASE64,
                Base64.encode((Byte[]) object));
    }

}