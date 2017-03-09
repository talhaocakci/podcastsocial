package com.javathlon.model.spreaker;


import com.google.gson.annotations.Expose;


public class ShowResponse {

    @Expose
    private Show show;

    /**
     * @return The show
     */
    public Show getShow() {
        return show;
    }

    /**
     * @param show The show
     */
    public void setShow(Show show) {
        this.show = show;
    }

}
