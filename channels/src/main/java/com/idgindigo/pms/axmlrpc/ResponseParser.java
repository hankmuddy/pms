package com.idgindigo.pms.axmlrpc;

import com.idgindigo.pms.axmlrpc.serializer.SerializerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The ResponseParser parses the response of an XmlRpc server to an object.
 *
 * @author Tim Roes
 */
public class ResponseParser {

    private static final String FAULT_CODE = "faultCode";
    private static final String FAULT_STRING = "faultString";
    private static final Logger logger = LoggerFactory.getLogger(ResponseParser.class);
    private static final Pattern CC_NUMBER = Pattern.compile("(<name>cc_number</name>\\n<value><string>\\d{4})\\d+(\\d{4}</string></value>)");
    private static final Pattern CVV = Pattern.compile("(<name>cc_cvv</name>\\n<value><string>)\\d+(</string></value>)");
    private static final Pattern OWNER = Pattern.compile("(<name>cc_owner</name>\\n<value><string>.{4}).+(.{4}</string></value>)");

    /**
     * The given InputStream must contain the xml response from an xmlrpc server.
     * This method extract the content of it as an object.
     *
     * @param response The InputStream of the server response.
     * @return The returned object.
     * @throws XmlRpcException       Will be thrown whenever something fails.
     * @throws XmlRpcServerException Will be thrown, if the server returns an error.
     */
    public Object parse(InputStream response) throws XmlRpcException {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(response);
            Element e = dom.getDocumentElement();
            if (logger.isTraceEnabled()) {
                try {
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    StreamResult result = new StreamResult(new StringWriter());
                    DOMSource source = new DOMSource(dom);
                    transformer.transform(source, result);
                    String msg = result.getWriter().toString();
                    msg = CC_NUMBER.matcher(msg).replaceAll(Call.SECURED);
                    msg = CVV.matcher(msg).replaceAll(Call.SECURED);
                    msg = OWNER.matcher(msg).replaceAll(Call.SECURED);
                    logger.trace(msg);
                } catch (TransformerException ex) {
                    logger.warn("Cannot trace response", ex);
                }
            }
            // Check for root tag
            if (!e.getNodeName().equals(XmlRpcClient.METHOD_RESPONSE)) {
                throw new XmlRpcException("MethodResponse root tag is missing.");
            }

            e = XmlUtil.getOnlyChildElement(e.getChildNodes());

            if (e.getNodeName().equals(XmlRpcClient.PARAMS)) {

                e = XmlUtil.getOnlyChildElement(e.getChildNodes());

                if (!e.getNodeName().equals(XmlRpcClient.PARAM)) {
                    throw new XmlRpcException("The params tag must contain a param tag.");
                }

                return getReturnValueFromElement(e);

            } else if (e.getNodeName().equals(XmlRpcClient.FAULT)) {

                @SuppressWarnings("unchecked")
                Map<String, Object> o = (Map<String, Object>) getReturnValueFromElement(e);

                throw new XmlRpcServerException((String) o.get(FAULT_STRING), (Integer) o.get(FAULT_CODE));

            }

            throw new XmlRpcException("The methodResponse tag must contain a fault or params tag.");

        } catch (Exception ex) {

            if (ex instanceof XmlRpcServerException)
                throw (XmlRpcServerException) ex;
            else
                throw new XmlRpcException("Error getting result from server.", ex);

        }

    }

    /**
     * This method takes an element (must be a param or fault element) and
     * returns the deserialized object of this param tag.
     *
     * @param element An param element.
     * @return The deserialized object within the given param element.
     * @throws XmlRpcException Will be thrown when the structure of the document
     *                         doesn't match the Xml-Rpc specification.
     */
    private Object getReturnValueFromElement(Element element) throws XmlRpcException {

        element = XmlUtil.getOnlyChildElement(element.getChildNodes());

        return SerializerHandler.getDefault().deserialize(element);

    }

}