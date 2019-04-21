package com.qf.tmall.util;

import com.github.pagehelper.StringUtil;
import com.qf.tmall.entity.Page;

public class SearchUtil {
    public String getOrderStr(Page page){
        StringBuffer stringBuffer = new StringBuffer();
        if(!StringUtil.isEmpty(page.getOrder())) {
            String strs[] = {"product_id","product_name","product_title","product_price","product_sale_price","product_create_date"};
            stringBuffer.append(" order by "+strs[Integer.parseInt(page.getOrder())]+" " + page.getDir());
        }
        return stringBuffer.toString();
    }
}
