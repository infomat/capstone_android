package com.conestogac.receipt_keeper.models;

import com.strongloop.android.loopback.Model;

/**
 * Created by hassannahhal on 2016-06-14.
 */
public class Receipt extends Model {

    private int id;
    private int customerId;
    private int storeId;
    private int categoryId;
    private String comment;
    private String date;
    private float total;
    private int tagId;

    //For Sync
    private String r_id;
    private String r_customerId;
    private String r_categoryId;
    private String r_tagId;
    private boolean isSync;

    public Receipt() {

    }


    public Receipt(int id, int customerId, int storeId, int categoryId, String comment, String date, float total, int tagId) {
        this.id = id;
        this.customerId = customerId;
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.comment = comment;
        this.date = date;
        this.total = total;
        this.tagId = tagId;
    }


    // Attribute Getters
    public int getLocalId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public float getTotal() {
        return total;
    }

    public int getTagId() {
        return tagId;
    }

    // Attribute Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerRemoteId(String customerId) {
        this.r_customerId = customerId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setCategoryId(int categroyId) {
        this.categoryId = categroyId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
