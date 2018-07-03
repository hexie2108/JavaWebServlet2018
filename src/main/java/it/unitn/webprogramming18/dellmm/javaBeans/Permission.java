/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webprogramming18.dellmm.javaBeans;

/**
 * @author luca_morgese
 */
public class Permission {

    private int id;
    private int listId;
    private int userId;
    private boolean addObject;
    private boolean deleteObject;
    private boolean modifyList;
    private boolean deleteList;


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
     * @return the listId
     */
    public int getListId() {
        return listId;
    }

    /**
     * @param listId the listId to set
     */
    public void setListId(int listId) {
        this.listId = listId;
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
     * @return the addObject
     */
    public boolean isAddObject() {
        return addObject;
    }

    /**
     * @param addObject the addObject to set
     */
    public void setAddObject(boolean addObject) {
        this.addObject = addObject;
    }

    /**
     * @return the deleteObject
     */
    public boolean isDeleteObject() {
        return deleteObject;
    }

    /**
     * @param deleteObject the deleteObject to set
     */
    public void setDeleteObject(boolean deleteObject) {
        this.deleteObject = deleteObject;
    }

    /**
     * @return the modifyList
     */
    public boolean isModifyList() {
        return modifyList;
    }

    /**
     * @param modifyList the modifyList to set
     */
    public void setModifyList(boolean modifyList) {
        this.modifyList = modifyList;
    }

    /**
     * @return the deleteList
     */
    public boolean isDeleteList() {
        return deleteList;
    }

    /**
     * @param deleteList the deleteList to set
     */
    public void setDeleteList(boolean deleteList) {
        this.deleteList = deleteList;
    }
}
