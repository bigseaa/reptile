package com.bigsea.model;

/**
 * @Description 封装抓取信息的对象
 * @author  sea
 * @Date 2018-12-22
 */

public class DataModel {

    private Long id;
    //详情地址
    private String url;
    //标题
    private String title;
    //发布日期yyyy-MM-dd
    private String publishDate;
    //所抓取源网站的分页列表首页URL
    private String dataListUrl;
    //来源网站名称
    private String dataSource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDataListUrl() {
        return dataListUrl;
    }

    public void setDataListUrl(String dataListUrl) {
        this.dataListUrl = dataListUrl;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

}
