package com.app.qothoo.driver.Model;

/**
 * Created by macbookpro on 05/07/2017.
 */

public class TokenObject {

    String token;
    String token_type;
    String expires_in;
    String userName;
    String issued;
    String expires;

    public String getToken() {
        return token;
    }

    /**
     * @param token @link token
     */
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_type() {
        return token_type;
    }

    /**
     * @param token_type
     */
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    /**
     * Expiration  in Date
     *
     * @param expires_in
     */
    public void setExpiresIn(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIssued() {
        return issued;
    }

    /**
     * @param issued
     */
    public void setIssued(String issued) {
        this.issued = issued;
    }

    /**
     * expire in time
     *
     * @param expires
     */
    public void setExpire(String expires) {
        this.expires = expires;
    }

    public String getExpires() {
        return expires;
    }
}
