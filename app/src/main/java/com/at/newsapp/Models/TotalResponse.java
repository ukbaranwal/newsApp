package com.at.newsapp.Models;

import com.google.gson.annotations.SerializedName;

import com.at.newsapp.Models.Articles;

import java.util.List;

public class TotalResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("totalResults")
    private String totalResults;
    @SerializedName("articles")
    private List<Articles> articlesList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumberOfResults() {
        return totalResults;
    }

    public void setNumberOfResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<Articles> getArticlesList() {
        return articlesList;
    }

    public void setArticlesList(List<Articles> articlesList) {
        this.articlesList = articlesList;
    }
}
