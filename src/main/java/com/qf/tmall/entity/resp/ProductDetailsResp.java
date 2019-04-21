package com.qf.tmall.entity.resp;

import com.qf.tmall.entity.Product;
import com.qf.tmall.entity.Property;
import com.qf.tmall.entity.domain.ProductDetailDomain;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsResp {
    private ProductDetailDomain productDetailDomain;
    private List<Property> propertyList = new ArrayList<Property>();

    public ProductDetailsResp(ProductDetailDomain productDetailDomain) {
        this.productDetailDomain = productDetailDomain;
    }

    public ProductDetailsResp() {
    }

    public ProductDetailDomain getProductDetailDomain() {
        return productDetailDomain;
    }

    public void setProductDetailDomain(ProductDetailDomain productDetailDomain) {
        this.productDetailDomain = productDetailDomain;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }
}
