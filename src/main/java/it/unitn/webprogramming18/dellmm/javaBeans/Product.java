/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.javaBeans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author luca_morgese
 */
public class Product implements Serializable {

    private int id;
    private String name;
    private String description;
    private String img;
    private String logo;
    private int categoryProductId;
    private int privateListId;


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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return the categoryProductId
     */
    public int getCategoryProductId() {
        return categoryProductId;
    }

    /**
     * @param categoryProductId the categoryProductId to set
     */
    public void setCategoryProductId(int categoryProductId) {
        this.categoryProductId = categoryProductId;
    }
    
    /**
     * @return the privateListId
     */
    public int getPrivateListId() {
        return privateListId;
    }

    /**
     * @param privateListId the privateListId to set
     */
    public void setPrivateListId(int privateListId) {
        this.privateListId = privateListId;
    }


}
