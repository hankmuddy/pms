package com.idgindigo.pms.axmlrpc;

/**
 * Will be thrown when a call to the server times out. The timeout can be
 * set via {@link XmlRpcClient#setTimeout(int)}.
 *
 * @author Tim Roes <mail@timroes.de>
 */
public class XmlRpcTimeoutException extends XmlRpcException {

    XmlRpcTimeoutException(String ex) {
        super(ex);
    }

}