package com.wrk.capitalnews.bean;

import java.util.List;

/**
 * Created by MrbigW on 2016/10/27.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 商场热卖的bean对象
 * -------------------=.=------------------------
 */

public class ShoppingMallBean {

    @Override
    public String toString() {
        return "ShoppingMallBean{" +
                "totalCount=" + totalCount +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", pageSize=" + pageSize +
                ", list=" + list +
                '}';
    }

    /**
     * totalCount : 28
     * currentPage : 2
     * totalPage : 3
     * pageSize : 10
     * list : [{"id":11,"name":"三星（SAMSUNG）S22B310B 21.5英寸宽屏LED背光液晶显示器","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_54695388N06e5b769.jpg","description":null,"price":799,"sale":7145},{"id":12,"name":"希捷（seagate）Expansion 新睿翼1TB 2.5英寸 USB3.0 移动硬盘 (STEA1000400)","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_5519fdd0N02706f5e.jpg","description":null,"price":399,"sale":402},{"id":13,"name":"联想（ThinkCentre）E73S（10EN001ACD） 小机箱台式电脑 （I3-4160T 4GB 500GB 核显 Win7）23.8英寸显示器","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55558580Nbb8545f3.jpg","description":null,"price":3599,"sale":571},{"id":14,"name":"新观 LED随身灯充电宝LED灯 LED护眼灯 阅读灯 下单备注颜色 无备注颜色随机发","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_5563e1d4Ncc22964e.jpg","description":null,"price":1,"sale":1652},{"id":15,"name":"旅行港版转换插头插座 港版充电器转换头 电源三转二","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_5583931aN31c0a2cf.jpg","description":null,"price":1,"sale":6547},{"id":16,"name":"极米 XGIMI Z4X LED投影仪 无屏电视 微型投影仪 家用","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55c1adc2Nf655de47.jpg","description":null,"price":2699,"sale":7778},{"id":17,"name":"惠普（HP）超薄系列 HP14g-ad005TX 14英寸超薄笔记本（i5-5200U 4G 500G 2G独显 DVD刻录 蓝牙 win8.1）银色","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55f9280eN31523bd6.jpg","description":null,"price":3299,"sale":9248},{"id":18,"name":"华硕（ASUS）经典系列X554LP 15.6英寸笔记本 （i5-5200U 4G 500G R5-M230 1G独显 蓝牙 Win8.1 黑色）","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_5604aab9N7a91521b.jpg","description":null,"price":2999,"sale":2906},{"id":19,"name":"海尔（Haier）云悦mini 2W(Win8.1)迷你主机(Intel四核J1900 4G 500G WIFI USB3.0 Win8.1)迷你电脑","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_rmd53edbf09N01b79405.jpg","description":null,"price":1699,"sale":6786},{"id":20,"name":"橙派（CPAD）E3 1231 V3/8G/GTX960-4G/游戏电脑主机/DIY组装机","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_rmd55619f15Nf1e9c48f.jpg","description":null,"price":3479,"sale":5211}]
     */

    private int totalCount;
    private int currentPage;
    private int totalPage;
    private int pageSize;
    /**
     * id : 11
     * name : 三星（SAMSUNG）S22B310B 21.5英寸宽屏LED背光液晶显示器
     * imgUrl : http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_54695388N06e5b769.jpg
     * description : null
     * price : 799
     * sale : 7145
     */

    private List<Wares> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Wares> getList() {
        return list;
    }

    public void setList(List<Wares> list) {
        this.list = list;
    }

    /**
     * 商品类
     */
    public static class Wares {

        @Override
        public String toString() {
            return "Wares{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", description='" + description + '\'' +
                    ", price=" + price +
                    ", sale=" + sale +
                    '}';
        }

        private int id;
        private String name;
        private String imgUrl;
        private String description;
        private double price;
        private int sale;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getSale() {
            return sale;
        }

        public void setSale(int sale) {
            this.sale = sale;
        }
    }
}
