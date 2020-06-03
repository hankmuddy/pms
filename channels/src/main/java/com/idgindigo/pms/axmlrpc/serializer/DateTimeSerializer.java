package com.idgindigo.pms.axmlrpc.serializer;

//import de.timroes.axmlrpc.XmlRpcException;
//import de.timroes.axmlrpc.XmlUtil;

import com.idgindigo.pms.axmlrpc.XmlRpcException;
import com.idgindigo.pms.axmlrpc.XmlUtil;
import com.idgindigo.pms.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author timroes
 */
public class DateTimeSerializer implements Serializer {

    private static final String DATETIME_FORMAT = "yyyyMMdd'T'HH:mm:ss";
    private final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat(DATETIME_FORMAT);

    @Override
    public Object deserialize(Element content) throws XmlRpcException {
        try {
            return DATE_FORMATER.parse(XmlUtil.getOnlyTextContent(content.getChildNodes()));
        } catch (ParseException ex) {
            throw new XmlRpcException("Unable to parse given date.", ex);
        }
    }

    @Override
    public XmlElement serialize(Object object) {
        return XmlUtil.makeXmlTag(SerializerHandler.TYPE_DATETIME,
                DATE_FORMATER.format(object));
    }

}
