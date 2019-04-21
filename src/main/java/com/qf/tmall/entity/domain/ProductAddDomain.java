package com.qf.tmall.entity.domain;


public class ProductAddDomain {

    private String product_name;
    private String product_title;
    private Integer product_category_id;
    private Double product_sale_price;
    private Double product_price;
    private Byte product_isEnabled;
    private String propertyJson;
    private String productSingleImageList;
    private String productDetailsImageList;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public Integer getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(Integer product_category_id) {
        this.product_category_id = product_category_id;
    }

    public Double getProduct_sale_price() {
        return product_sale_price;
    }

    public void setProduct_sale_price(Double product_sale_price) {
        this.product_sale_price = product_sale_price;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }

    public Byte getProduct_isEnabled() {
        return product_isEnabled;
    }

    public void setProduct_isEnabled(Byte product_isEnabled) {
        this.product_isEnabled = product_isEnabled;
    }

    public String getPropertyJson() {
        return propertyJson;
    }

    public void setPropertyJson(String propertyJson) {
        this.propertyJson = propertyJson;
    }

    public String getProductSingleImageList() {
        return productSingleImageList;
    }

    public void setProductSingleImageList(String productSingleImageList) {
        this.productSingleImageList = productSingleImageList;
    }

    public String getProductDetailsImageList() {
        return productDetailsImageList;
    }

    public void setProductDetailsImageList(String productDetailsImageList) {
        this.productDetailsImageList = productDetailsImageList;
    }
}
