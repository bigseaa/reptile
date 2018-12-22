package com.bigsea;

import com.bigsea.model.DataModel;
import com.bigsea.utils.DataUtil;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description 用以抓取目标url中的列表的数据，根据具体的网站的布局，修改writeDataList(Source listSource)方法
 * @author sea
 * @date 2018-12-22
 */
public class ReptileDemo {

    //抓取的日期
    private String dateStr = "2018-12-21";
    //抓取到的数据总数
    private int catchTotol = 0;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public void extract(String listUrl,
                        String strBeforePageNum,
                        int firstPageNum,
                        String strAfterPageNum) throws Exception {
        int totel = 0;

        boolean end = false;
        for(int j = firstPageNum; ; j++) {//翻页循环开始
            //避免请求的频率太快，让线程睡眠3秒
            Thread.sleep(3000);
            //String listPageUrl = rootUrl + pageNumFrontStr + j + pageNumBehindStr;
            String listPageUrl = strBeforePageNum + j + strAfterPageNum;
            Source listSource = DataUtil.getSource(listPageUrl);
            if(listSource == null) {
                break;
            }
            System.out.println("*********************** 第"+j+"页 ***********************");
            List dataList = writeDataList(listSource);

            /**
             * STEP4:dataList为一个集合，暂时存放当前一页抓取到的数据，遍历其中的数据
             * 1.如果dataList当中没有数据，break，结束当前抓取(后面的翻页放弃掉)；
             * 2.遍历dataList中的数据，如果有数据的时间小于当前时间的，结束当前抓取(后面的翻页放弃掉)。
             */
            if(dataList.size() < 1) {
                System.out.println("*********************** 第"+j+"页为空白页 ***********************");
                break;
            }

            Map<String, String> returnMap = readDataList(dataList, listUrl);
            int row = Integer.valueOf(returnMap.get("row"));
            boolean isEnd = Boolean.valueOf(returnMap.get("isEnd"));
            if(isEnd == true) {
                end = true;
            }
            totel += row;
            System.out.println("*********************** 第" + j + "页 "+ dateStr + "共有"+row+"条数据 ***********************");
            if(end) {//结束
                break;
            }
        }//end 翻页循环
        System.out.println(listUrl+" 总数："+totel);
    }

    /**
     * 解析由http请求返回的数据
     * @param listSource
     * @return
     * @throws Exception
     */
    private List<DataModel> writeDataList(Source listSource) throws Exception {
        List<DataModel> dataList = new ArrayList<>();
        List<Element> ulElements = listSource.getAllElementsByClass("new-list");
        Element ulElement = (Element) ulElements.get(0);
        List liElements = ulElement.getAllElements(HTMLElementName.LI);
        for(int i = 0; i < liElements.size(); i++) {
            Element element = (Element)liElements.get(i);
            if("line".equals(element.getAttributeValue("class"))) {
                continue;
            }
            DataModel dataModel = new DataModel();
            List liChildrenElements = element.getChildElements();
            //时间
            Element spanElement = (Element) liChildrenElements.get(0);
            String timeStr = spanElement.getContent().toString().trim();
            //标题与url
            Element aElement = (Element) liChildrenElements.get(1);
            String title = aElement.getAttributeValue("title");
            String href = aElement.getAttributeValue("href");

            dataModel.setUrl(href);
            dataModel.setTitle(title);
            //给时间赋值
            timeStr = getPublishDateStrFormat(timeStr);
            dataModel.setPublishDate(timeStr);

            dataList.add(dataModel);

        }
        return dataList;
    }

    /**
     * 读取存在于内存中dataList的值
     * @param dataList, listUrl
     * @return Map<String, String>
     * @throws Exception
     */
    private Map<String, String> readDataList(List<DataModel> dataList, String listUrl) throws Exception {
        Map<String, String> returnMap = new HashMap<>();
        int row = 0;
        boolean isEnd = false;
        for(DataModel dataModel : dataList) {
            /**
             * 若果抓取的数据的时间与设置的时间不相等，需进行判断，如果小于所设置的时间则没有抓取的必要
             */
            if(sdf.parse(dateStr).compareTo(sdf.parse(dataModel.getPublishDate())) != 0) {
                if(sdf.parse(dataModel.getPublishDate()).before(sdf.parse(dateStr))) {
                    isEnd = true;
                    break;
                }
            }
            row++;
            dataModel.setDataListUrl(listUrl);
            System.out.println("第" + row + "条");
            System.out.println("标题:" + dataModel.getTitle());
            System.out.println("URL:" + dataModel.getUrl());
            System.out.println("列表地址：" + dataModel.getDataListUrl());
            System.out.println("发布日期：" + dataModel.getPublishDate());

            /**
             * 如果从当前数据（对象）的url中无法获取到资源，则不入库改条数据,遍历dataList中的下一条数据
             */
            Source source = DataUtil.getSource(listUrl);
            if(source == null){
                continue;
            }
            dataModel.setDataSource("测试");
            System.out.println("来源：" + dataModel.getDataSource());

            catchTotol ++;
        }
        returnMap.put("row", String.valueOf(row));
        returnMap.put("isEnd", String.valueOf(isEnd));
        return returnMap;
    }

    /**
     * 抓取的时间格式不同的网站、不同的的栏目会有不同的格式，所以需要根据具体场景截取
     * @param publishDate
     * @return
     */
    private String getPublishDateStrFormat(String publishDate) {
        return publishDate.substring(1, 11);
    }

}
