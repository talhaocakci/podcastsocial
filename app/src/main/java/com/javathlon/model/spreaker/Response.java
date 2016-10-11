
package com.javathlon.model.spreaker;


import com.google.gson.annotations.Expose;


public class Response {

    @Expose
    private Pager pager;

    /**
     * 
     * @return
     *     The pager
     */
    public Pager getPager() {
        return pager;
    }

    /**
     * 
     * @param pager
     *     The pager
     */
    public void setPager(Pager pager) {
        this.pager = pager;
    }

}
