package com.cn.xa.qyw.entiy;

public class CommodityOrderData {
//    {
//        "areaId": 1,
//            "areaName": "11",
//            "contacts": "1111",
//            "productDisc": "",
//            "productId": 2,
//            "productName": "hahahah",
//            "productNum": 1,
//            "productPic": "upload/1532872352487.jpg",
//            "productPrice": 100,
//            "sellerAddress": "111",
//            "sellerId": 3,
//            "sellerName": "1111",
//            "sellerPhone": "111",
//            "typeName": "模块1",
//            "updateTime": 1533452139000
//    }

    private int areaId;  //商圈Id
    private String areaName;  //商圈名成
    private String contacts; //商户联系人
    private String productDisc;  //商品描述
    private int productId;   //商品Id
    private String productName; //商品名称
    private int productNum;  //商品数量
    private String productPic; //商品图片
    private int productPrice; //商品价格
    private int specifications; //商品规格
    private String sellerAddress; //商户地址
    private int sellerId;   //商户id
    private String sellerName; //商户名
    private String sellerPhone; //商户电话
    private String typeName;  //商品类型
    private long updateTime; //更新时间

    public int getSpecifications() {
        return specifications;
    }

    public void setSpecifications(int specifications) {
        this.specifications = specifications;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getProductDisc() {
        return productDisc;
    }

    public void setProductDisc(String productDisc) {
        this.productDisc = productDisc;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
