package com.idgindigo.pms.channel.wubook;

/**
 * @author vomel
 * @since 17.12.13 16:05
 */
public class WubookException extends RuntimeException {
    private static final long serialVersionUID = 3555252453867592528L;
    private final Integer code;

    public WubookException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public WubookException(String message, Exception e) {
        super(message, e);
        code = null;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return super.toString() + ", code = " + code + ", " + getMessage();
    }
}
