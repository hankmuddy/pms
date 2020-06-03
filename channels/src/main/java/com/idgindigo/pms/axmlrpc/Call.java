package com.idgindigo.pms.axmlrpc;

import com.idgindigo.pms.axmlrpc.serializer.SerializerHandler;
import com.idgindigo.pms.axmlrpc.xmlcreator.SimpleXmlCreator;
import com.idgindigo.pms.axmlrpc.xmlcreator.XmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * A Call object represents a call of a remote methode.
 * It contains the name of the method to be called and the parameters to use
 * in this remote procedure call. To send it over the network the method getXml
 * returns an xml representation according to the Xml-Rpc specification as a String.
 *
 * @author Tim Roes
 */
public class Call {
    private static final Logger logger = LoggerFactory.getLogger(Call.class);
    private static final Pattern GET_TOKEN_PASSWORD = Pattern.compile("(<param>\\n<value>\\n<string>).*(</string>\\n</value>\\n</param>\\n</params>)");
    private static final Pattern FETCH_CC_DATA = Pattern.compile("(<param>\\n<value>\\n<string>).*(</string>\\n</value>\\n</param>\\n\\n<param>\\n<value>\\n<string>).*(</string>\\n</value>\\n</param>\\n</params>)");
    public static final String SECURED = "$1[secured]$2";
    public static final String SECURED_2 = SECURED + "[secured]$3";

    private String method;
    private Object[] params;

    /**
     * Create a new method call with the given name and no parameters.
     *
     * @param method The method to be called.
     */
    public Call(String method) {
        this(method, null);
    }

    /**
     * Create a new method call with the given name and parameters.
     *
     * @param method The method to be called.
     * @param params An array of parameters for the method.
     */
    public Call(String method, Object[] params) {
        this.method = method;
        this.params = params;
    }

    /**
     * Return an xml representation of the method call as specified in
     * http://www.xmlrpc.com/spec. If flags have been set in the XmlRpcClient
     * the returning xml does not comply strict to the standard.
     *
     * @return The string of the xml representing this call.
     * @throws XmlRpcException Will be thrown whenever the xml representation cannot
     *                         be build without errors.
     * @see XmlRpcClient
     */
    public String getXml() throws XmlRpcException {

        SimpleXmlCreator creator = new SimpleXmlCreator();

        XmlElement methodCall = new XmlElement(XmlRpcClient.METHOD_CALL);
        creator.setRootElement(methodCall);

        XmlElement methodName = new XmlElement(XmlRpcClient.METHOD_NAME);
        methodName.setContent(method);
        methodCall.addChildren(methodName);

        if (params != null && params.length > 0) {
            XmlElement params = new XmlElement(XmlRpcClient.PARAMS);
            methodCall.addChildren(params);

            for (Object o : this.params) {
                params.addChildren(getXmlParam(o));
            }
        }

        String result = creator.toString();
        if (logger.isTraceEnabled()) {
            logger.trace("Prepared XMLRPC request:");
            if (result.contains("get_token")) {
                logger.trace(GET_TOKEN_PASSWORD.matcher(result).replaceAll(SECURED));
            } else if (result.contains("fetch_cc")) {
                logger.trace(FETCH_CC_DATA.matcher(result).replaceAll(SECURED_2));
            } else {
                logger.trace(result);
            }
        }
        return result;
    }

    /**
     * Generates the param xml tag for a specific parameter object.
     *
     * @param o The parameter object.
     * @return The object serialized into an xml tag.
     * @throws XmlRpcException Will be thrown if the serialization failed.
     */
    private XmlElement getXmlParam(Object o) throws XmlRpcException {
        XmlElement param = new XmlElement(XmlRpcClient.PARAM);
        XmlElement value = new XmlElement(XmlRpcClient.VALUE);
        param.addChildren(value);
        value.addChildren(SerializerHandler.getDefault().serialize(o));
        return param;
    }

}
