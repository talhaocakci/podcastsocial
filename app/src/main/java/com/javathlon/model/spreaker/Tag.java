package com.javathlon.model.spreaker;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Tag {

    @SerializedName("tag_id")
    @Expose
    private Long tagId;
    @Expose
    private String name;

    /**
     * @return The tagId
     */
    public Long getTagId() {
        return tagId;
    }

    /**
     * @param tagId The tag_id
     */
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

}
