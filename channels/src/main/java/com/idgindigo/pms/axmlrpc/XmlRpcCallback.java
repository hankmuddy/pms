package com.idgindigo.pms.axmlrpc;

/**
 * The XmlRpcCallback interface must be implemented by a listener for an
 * asynchronous call to a server method.
 * When the server responds, the corresponding method on the listener is called.
 *
 * @author Tim Roes
 */
public interface XmlRpcCallback {

    /**
     * This callback is called whenever the server successfully responds.
     *
     * @param id     The id as returned by the XmlRpcClient.asyncCall(..) method for this request.
     * @param result The Object returned from the server.
     */
    public void onResponse(long id, Object result);

    /**
     * This callback is called whenever an error occurs during the method call.
     *
     * @param id    The id as returned by the XmlRpcClient.asyncCall(..) method for this request.
     * @param error The error occured.
     */
    public void onError(long id, XmlRpcException error);

    /**
     * This callback is called whenever the server returns an error.
     *
     * @param id    The id as returned by the XmlRpcClient.asyncCall(..) method for this request.
     * @param error The error returned from the server.
     */
    public void onServerError(long id, XmlRpcServerException error);

}
