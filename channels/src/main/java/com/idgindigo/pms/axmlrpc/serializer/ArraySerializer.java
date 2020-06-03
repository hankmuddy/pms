package com.idgindigo.pms.axmlrpc.serializer;

//import de.timroes.axmlrpc.XmlRpcException;
//import de.timroes.axmlrpc.XmlRpcRuntimeException;
//import de.timroes.axmlrpc.XmlUtil;

import com.idgindigo.pms.axmlrpc.XmlRpcException;
import com.idgindigo.pms.axmlrpc.XmlRpcRuntimeException;
import com.idgindigo.pms.axmlrpc.XmlUtil;
import com.idgindigo.pms.axmlrpc.xmlcreator.XmlElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Roes
 */
public class ArraySerializer implements Serializer {

    private static final String ARRAY_DATA = "data";
    private static final String ARRAY_VALUE = "value";

    public Object deserialize(Element content) throws XmlRpcException {

        List<Object> list = new ArrayList<Object>();

        Element data = XmlUtil.getOnlyChildElement(content.getChildNodes());

        if (!ARRAY_DATA.equals(data.getNodeName())) {
            throw new XmlRpcException("The array must contain one data tag.");
        }

        // Deserialize every array element
        Node value;
        for (int i = 0; i < data.getChildNodes().getLength(); i++) {

            value = data.getChildNodes().item(i);

            // Strip only whitespace text elements and comments
            if (value == null || (value.getNodeType() == Node.TEXT_NODE
                    && value.getNodeValue().trim().length() <= 0)
                    || value.getNodeType() == Node.COMMENT_NODE)
                continue;

            if (value.getNodeType() != Node.ELEMENT_NODE) {
                throw new XmlRpcException("Wrong element inside of array.");
            }

            list.add(SerializerHandler.getDefault().deserialize((Element) value));

        }

        return list.toArray();
    }

    public XmlElement serialize(Object object) {

        Iterable<?> iter = (Iterable<?>) object;
        XmlElement array = new XmlElement(SerializerHandler.TYPE_ARRAY);
        XmlElement data = new XmlElement(ARRAY_DATA);
        array.addChildren(data);

        try {

            XmlElement e;
            for (Object obj : iter) {
                e = new XmlElement(ARRAY_VALUE);
                e.addChildren(SerializerHandler.getDefault().serialize(obj));
                data.addChildren(e);
            }

        } catch (XmlRpcException ex) {
            throw new XmlRpcRuntimeException(ex);
        }

        return array;

    }

}