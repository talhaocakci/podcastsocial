package com.javathlon.model.spreaker;


import com.google.gson.annotations.Expose;


public class ShowResult {

    @Expose
    private ShowResponse response;

    /**
     * @return The response
     */
    public ShowResponse getResponse() {
        return response;
    }

    /**
     * @param response The response
     */
    public void setResponse(ShowResponse response) {
        this.response = response;
    }

}
