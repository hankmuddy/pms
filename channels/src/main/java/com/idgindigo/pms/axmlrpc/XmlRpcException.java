package com.idgindigo.pms.axmlrpc;

/**
 * The exception is thrown whenever the remote procedure call fails in some point.
 *
 * @author Tim Roes
 */
public class XmlRpcException extends Exception {

    public XmlRpcException() {
        super();
    }

    public XmlRpcException(Exception ex) {
        super(ex);
    }

    public XmlRpcException(String ex) {
        super(ex);
    }

    public XmlRpcException(String msg, Exception ex) {
        super(msg, ex);
    }

}
