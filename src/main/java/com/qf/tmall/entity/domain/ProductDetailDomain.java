package com.qf.tmall.entity.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qf.tmall.entity.Category;
import com.qf.tmall.entity.ProductImage;
import io.swagger.annotations.ApiModelProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductDetailDomain {
    @ApiModelProperty("产品id")
    private Integer product_id;
    @ApiModelProperty("产品名称")
    private String product_name;
    @ApiModelProperty("产品标题")
    private String product_title;
    @ApiModelProperty("产品价格")
    private Double product_price;
    @ApiModelProperty("产品促销价")
    private Double product_sale_price;
    @ApiModelProperty("产品创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date product_create_date;
    @ApiModelProperty("产品销售状况")
    private Byte product_isEnabled;
    @ApiModelProperty("产品属于类型")
    private CategoryDomain product_category;
    private List<ProductImage> singleProductImageList;
    private List<ProductImage> detailProductImageList;

    public List<ProductImage> getSingleProductImageList() {
        return singleProductImageList;
    }

    public void setSingleProductImageList(List<ProductImage> singleProductImageList) {
        this.singleProductImageList = singleProductImageList;
    }

    public List<ProductImage> getDetailProductImageList() {
        return detailProductImageList;
    }

    public void setDetailProductImageList(List<ProductImage> detailProductImageList) {
        this.detailProductImageList = detailProductImageList;
    }

    public CategoryDomain getProduct_category() {
        return product_category;
    }

    public void setProduct_category(CategoryDomain product_category) {
        this.product_category = product_category;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

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

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }

    public Double getProduct_sale_price() {
        return product_sale_price;
    }

    public void setProduct_sale_price(Double product_sale_price) {
        this.product_sale_price = product_sale_price;
    }

    public String getProduct_create_date() {
        if(product_create_date != null){
            SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
            return time.format(product_create_date);
        }
        return null;
    }

    public void setProduct_create_date(String product_create_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.product_create_date = sdf.parse(product_create_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Byte getProduct_isEnabled() {
        return product_isEnabled;
    }

    public void setProduct_isEnabled(Byte product_isEnabled) {
        this.product_isEnabled = product_isEnabled;
    }
}
