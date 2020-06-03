package com.idgindigo.pms.axmlrpc;

/**
 * @author Tim Roes
 */
public class XmlRpcRuntimeException extends RuntimeException {

    public XmlRpcRuntimeException(String ex) {
        super(ex);
    }

    public XmlRpcRuntimeException(Exception ex) {
        super(ex);
    }

}