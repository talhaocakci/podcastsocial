package com.javathlon.memsoft;

public class ResponseHolder {

    private int responseCode;

    private String responseText;


    public ResponseHolder() {
        super();
    }

    public ResponseHolder(int responseCode, String responseText) {
        super();
        this.responseCode = responseCode;
        this.responseText = responseText;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }


}
