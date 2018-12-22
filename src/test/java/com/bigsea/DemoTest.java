package com.bigsea;

/**
 * @description 调用抓取方法测试类
 * @author sea
 * @date 2018-12-22
 */
public class DemoTest {

    public static void main(String[] args) {
        //所抓取列表首页的url
        String listUrl = "http://news.cnstock.com/news/sns_yw/index.html";
        //分页url前面一段字符串
        String strBeforePageNum = "http://news.cnstock.com/news/sns_yw/";
        //所抓取的首页页码
        int firstPageNum = 1;
        //分页url后面的一段的字符串
        String strAfterPageNum = "";

        ReptileDemo reptileDemo = new ReptileDemo();
        try {
            reptileDemo.extract(listUrl, strBeforePageNum, firstPageNum, strAfterPageNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
