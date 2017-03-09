package com.javathlon.model.spreaker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Pager {

    @SerializedName("current_page")
    @Expose
    private Long currentPage;
    @SerializedName("first_page")
    @Expose
    private Long firstPage;
    @SerializedName("last_page")
    @Expose
    private Long lastPage;
    @SerializedName("results_count")
    @Expose
    private Long resultsCount;
    @Expose
    private List<Result> results = new ArrayList<Result>();

    /**
     * @return The currentPage
     */
    public Long getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage The current_page
     */
    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return The firstPage
     */
    public Long getFirstPage() {
        return firstPage;
    }

    /**
     * @param firstPage The first_page
     */
    public void setFirstPage(Long firstPage) {
        this.firstPage = firstPage;
    }

    /**
     * @return The lastPage
     */
    public Long getLastPage() {
        return lastPage;
    }

    /**
     * @param lastPage The last_page
     */
    public void setLastPage(Long lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * @return The resultsCount
     */
    public Long getResultsCount() {
        return resultsCount;
    }

    /**
     * @param resultsCount The results_count
     */
    public void setResultsCount(Long resultsCount) {
        this.resultsCount = resultsCount;
    }

    /**
     * @return The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

}
