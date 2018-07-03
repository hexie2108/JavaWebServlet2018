/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.javaBeans;

import java.sql.Timestamp;

/**
 * @author luca_morgese
 */
public class Log {

    private int id;
    private int productId;
    private int userId;
    private Timestamp last1;
    private Timestamp last2;
    private Timestamp last3;
    private Timestamp last4;


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
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the last1
     */
    public Timestamp getLast1() {
        return last1;
    }

    /**
     * @param last1 the last1 to set
     */
    public void setLast1(Timestamp last1) {
        this.last1 = last1;
    }

    /**
     * @return the last2
     */
    public Timestamp getLast2() {
        return last2;
    }

    /**
     * @param last2 the last2 to set
     */
    public void setLast2(Timestamp last2) {
        this.last2 = last2;
    }

    /**
     * @return the last3
     */
    public Timestamp getLast3() {
        return last3;
    }

    /**
     * @param last3 the last3 to set
     */
    public void setLast3(Timestamp last3) {
        this.last3 = last3;
    }

    /**
     * @return the last4
     */
    public Timestamp getLast4() {
        return last4;
    }

    /**
     * @param last4 the last4 to set
     */
    public void setLast4(Timestamp last4) {
        this.last4 = last4;
    }
}
