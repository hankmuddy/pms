package com.idgindigo.pms.restutils.exception.handler;

/**
 * @author vomel
 * @since 29.10.13 13:01
 */
public class ExceptionMessage {

    private String code;
    private String source;
    private Boolean success = false;

    public ExceptionMessage() {
    }

    public ExceptionMessage(String code) {
        this.code = code;
    }

    public ExceptionMessage(String code, String source) {
        this.code = code;
        this.source = source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

