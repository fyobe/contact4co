package com.vouov.ailk.app.model;

import java.util.List;

/**
 * User: yuml
 * Date: 13-1-18
 * Time: 下午10:55
 */
public class Pagination<T>{
    private int totalPage;
    private int currentPage;
    private List<T> data;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
