package com.idgindigo.pms.channel.wubook;


/**
 * @author vomel
 * @since 08.01.14 15:57
 */
public class WubookAccount {
    private String username;
    private String password;
    private String lcode;

    public WubookAccount() {
    }

    public WubookAccount(String username, String password, String lcode) {
        this.username = username;
        this.password = password;
        this.lcode = lcode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLcode() {
        return lcode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WubookAccount{");
        sb.append("lcode='").append(lcode).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
