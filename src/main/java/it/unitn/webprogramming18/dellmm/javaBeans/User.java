/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.javaBeans;

import java.io.Serializable;

/**
 * @author luca_morgese
 */
public class User implements Serializable
{

        private int id;
        private String name;
        private String surname;
        private String password;
        private String email;
        private String img;
        private boolean isAdmin;
        private String verifyEmailLink;
        private String resetPwdEmailLink;
        private boolean acceptedPrivacy;
        private Long lastLoginTimeMillis;
        private String keyForFastLogin;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return the isAdmin
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin the isAdmin to set
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return the verifyMailLink
     */
    public String getVerifyEmailLink() {
        return verifyEmailLink;
    }

    /**
     * @param verifyEmailLink the verifyMailLink to set
     */
    public void setVerifyEmailLink(String verifyEmailLink) {
        this.verifyEmailLink = verifyEmailLink;
    }

    /**
     * @return the resetPwdEmailLink
     */
    public String getResetPwdEmailLink() {
        return resetPwdEmailLink;
    }

        /**
         * @param resetPwdEmailLink the resetPwdEmailLink to set
         */
        public void setResetPwdEmailLink(String resetPwdEmailLink)
        {
                this.resetPwdEmailLink = resetPwdEmailLink;
        }

        /**
         * @return the acceptedPrivacy
         */
        public boolean isAcceptedPrivacy()
        {
                return acceptedPrivacy;
        }

        /**
         * @param acceptedPrivacy the acceptedPrivacy to set
         */
        public void setAcceptedPrivacy(boolean acceptedPrivacy)
        {
                this.acceptedPrivacy = acceptedPrivacy;
        }


        /**
         * @return the keyForFastLogin
         */
        public String getKeyForFastLogin()
        {
                return keyForFastLogin;
        }

        /**
         * @param keyForFastLogin the keyForFastLogin to set
         */
        public void setKeyForFastLogin(String keyForFastLogin)
        {
                this.keyForFastLogin = keyForFastLogin;
        }

        /**
         * @return the lastLoginTimeMillis
         */
        public Long getLastLoginTimeMillis()
        {
                return lastLoginTimeMillis;
        }

        /**
         * @param lastLoginTimeMillis the lastLoginTimeMillis to set
         */
        public void setLastLoginTimeMillis(Long lastLoginTimeMillis)
        {
                this.lastLoginTimeMillis = lastLoginTimeMillis;
        }
}
