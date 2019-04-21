package com.qf.tmall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qf.tmall.controller.BaseController;
import com.qf.tmall.entity.*;
import com.qf.tmall.entity.domain.*;
import com.qf.tmall.entity.resp.ListResp;
import com.qf.tmall.entity.resp.ProductDetailsResp;
import com.qf.tmall.entity.resp.ProductResp;
import com.qf.tmall.service.*;
import com.qf.tmall.util.JsonUtil;
import com.qf.tmall.util.PageUtil;
import com.qf.tmall.util.SearchUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author ACI
 * @Title: ProductController
 * @ProjectName Tmall_shop
 * @Description: TODO
 * @date 2019/2/20 10:47
 */
@Api(tags = "后台管理-产品页")
@Controller
public class ProductController extends BaseController {
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "productImageService")
    private ProductImageService productImageService;
    @Resource(name = "propertyService")
    private PropertyService propertyService;
    @Resource(name = "propertyValueService")
    private PropertyValueService propertyValueService;
    @Resource(name = "lastIDService")
    private LastIDService lastIDService;

    //转到后台管理-产品页-ajax
    @ApiOperation(value = "跳转产品管理页面")
    @RequestMapping(value = "admin/product",method = RequestMethod.GET)
    public String goToPage(HttpSession session) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if(adminId == null){
            return "admin/include/loginMessage";
        }

        logger.info("转到后台管理-产品页-ajax方式");
        return "admin/productManagePage";
    }

    @ResponseBody
    @ApiOperation(value = "获取列表与分页信息")
    @RequestMapping(value = "admin/list",method = RequestMethod.POST)
    public ProductResp<List<ProductDomain>> goToPage(@RequestBody Page page) {
        ProductResp<List<ProductDomain>> data = new ProductResp<List<ProductDomain>>();
        logger.info("检查管理员权限");
        logger.info("获取产品分类列表");
        PageUtil pageUtil = new PageUtil(page.getPageNum()-1, page.getPageSize());
        List<ProductDomain> list=new ArrayList<ProductDomain>();
        String orderStr = new SearchUtil().getOrderStr(page);
        List<Product> productList = productService.getList(null, null, null, pageUtil,orderStr,page.getSearch());
        if(productList != null){
            for(Product product:productList){
                ProductDomain productDomain = new ProductDomain();
                try {
                    BeanUtils.copyProperties(product, productDomain);
                    productDomain.setProduct_create_date(product.getProduct_create_date());
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                list.add(productDomain);
            }
        }
        data.setData(list);
        logger.info("获取产品总数量");
        Product product = new Product();
        product.setProduct_name(page.getSearch());
        Integer productCount = productService.getTotal(product, null);
        pageUtil.setTotal(productCount);
        data.setPageUtil(pageUtil);
        logger.info("转到后台管理-产品页-ajax方式");
        return data;
    }

    //转到后台管理-产品添加页-ajax
    @ApiOperation(value = "跳转产品添加页")
    @RequestMapping(value = "admin/product/new",method = RequestMethod.GET)
    public String goToAddPage(HttpSession session,Map<String, Object> map){
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if(adminId == null){
            return "admin/include/loginMessage";
        }
        logger.info("获取分类列表");
        List<Category> categoryList = categoryService.getList(null,null);
        map.put("categoryList",categoryList);
        logger.info("获取第一个分类信息对应的属性列表");
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(categoryList.get(0)),null);
        map.put("propertyList",propertyList);

        logger.info("转到后台管理-产品添加页-ajax方式");
        return "admin/include/productDetails";
    }
    //转到后台管理-产品详情页-ajax
    @RequestMapping(value="admin/product/{pid}",method = RequestMethod.GET)
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer pid/* 产品ID */) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if(adminId == null){
            return "admin/include/loginMessage";
        }

        logger.info("获取product_id为{}的产品信息",pid);
        Product product = productService.get(pid);
        logger.info("获取产品详情-图片信息");
        Integer product_id =product.getProduct_id();
        List<ProductImage> productImageList = productImageService.getList(product_id, null, null);
        List<ProductImage> singleProductImageList = new ArrayList<>(5);
        List<ProductImage> detailsProductImageList = new ArrayList<>(8);
        for (ProductImage productImage : productImageList) {
            if (productImage.getProductImage_type() == 0) {
                singleProductImageList.add(productImage);
            } else {
                detailsProductImageList.add(productImage);
            }
        }
        product.setSingleProductImageList(singleProductImageList);
        product.setDetailProductImageList(detailsProductImageList);
        map.put("product",product);
        logger.info("获取产品详情-属性值信息");
        List<PropertyValue> propertyValueList = propertyValueService.getList(new PropertyValue().setPropertyValue_product(product),null);
        logger.info("获取产品详情-分类信息对应的属性列表");
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(product.getProduct_category()),null);
        logger.info("属性列表和属性值列表合并");
        for(Property property : propertyList){
            for(PropertyValue propertyValue : propertyValueList){
                if(property.getProperty_id().equals(propertyValue.getPropertyValue_property().getProperty_id())){
                    List<PropertyValue> property_value_item = new ArrayList<>(1);
                    property_value_item.add(propertyValue);
                    property.setPropertyValueList(property_value_item);
                    break;
                }
            }
        }
        map.put("propertyList",propertyList);
        logger.info("获取分类列表");
        List<Category> categoryList = categoryService.getList(null,null);
        map.put("categoryList",categoryList);

        logger.info("转到后台管理-产品详情页-ajax方式");
        return "admin/include/productDetails";
    }

    //添加产品信息-ajax.
    @ApiOperation(value = "添加产品信息")
    @ResponseBody
    @RequestMapping(value = "admin/product", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String addProduct(@RequestParam String product_name/* 产品名称 */,
                             @RequestParam String product_title/* 产品标题 */,
                             @RequestParam Integer product_category_id/* 产品类型ID */,
                             @RequestParam Double product_sale_price/* 产品最低价 */,
                             @RequestParam Double product_price/* 产品最高价 */,
                             @RequestParam Byte product_isEnabled/* 产品状态 */,
                             @RequestParam String propertyJson/* 产品属性JSON */,
                             @RequestParam(required = false) String[] productSingleImageList/*产品预览图片名称数组*/,
                             @RequestParam(required = false) String[] productDetailsImageList/*产品详情图片名称数组*/) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合产品信息");
        Product product = new Product()
                .setProduct_name(product_name)
                .setProduct_title(product_title)
                .setProduct_category(new Category().setCategory_id(product_category_id))
                .setProduct_sale_price(product_sale_price)
                .setProduct_price(product_price)
                .setProduct_isEnabled(product_isEnabled)
                .setProduct_create_date(new Date());
        logger.info("添加产品信息");
        boolean yn = productService.add(product);
        if (!yn) {
            logger.warn("产品添加失败！事务回滚");
            jsonObject.put("success", false);
            throw new RuntimeException();
        }
        int product_id = lastIDService.selectLastID();
        logger.info("添加成功！,新增产品的ID值为：{}", product_id);

        JSONObject object = JSON.parseObject(propertyJson);
        Set<String> propertyIdSet = object.keySet();
        if (propertyIdSet.size() > 0) {
            logger.info("整合产品子信息-产品属性");
            List<PropertyValue> propertyValueList = new ArrayList<>(5);
            for (String key : propertyIdSet) {
                String value = object.getString(key);
                PropertyValue propertyValue = new PropertyValue()
                        .setPropertyValue_value(value)
                        .setPropertyValue_property(new Property().setProperty_id(Integer.valueOf(key)))
                        .setPropertyValue_product(new Product().setProduct_id(product_id));
                propertyValueList.add(propertyValue);
            }
            logger.info("共有{}条产品属性数据", propertyValueList.size());
            yn = propertyValueService.addList(propertyValueList);
            if (yn) {
                logger.info("产品属性添加成功！");
            } else {
                logger.warn("产品属性添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        if (productSingleImageList != null && productSingleImageList.length > 0) {
            logger.info("整合产品子信息-产品预览图片");
            List<ProductImage> productImageList = new ArrayList<>(5);
            for (String imageName : productSingleImageList) {
                productImageList.add(new ProductImage()
                        .setProductImage_type((byte) 0)
                        .setProductImage_src(imageName.substring(imageName.lastIndexOf("/") + 1))
                        .setProductImage_product(new Product().setProduct_id(product_id))
                );
            }
            logger.info("共有{}条产品预览图片数据", productImageList.size());
            yn = productImageService.addList(productImageList);
            if (yn) {
                logger.info("产品预览图片添加成功！");
            } else {
                logger.warn("产品预览图片添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }

        if (productDetailsImageList != null && productDetailsImageList.length > 0) {
            logger.info("整合产品子信息-产品详情图片");
            List<ProductImage> productImageList = new ArrayList<>(5);
            for (String imageName : productDetailsImageList) {
                productImageList.add(new ProductImage()
                        .setProductImage_type((byte) 1)
                        .setProductImage_src(imageName.substring(imageName.lastIndexOf("/") + 1))
                        .setProductImage_product(new Product().setProduct_id(product_id))
                );
            }
            logger.info("共有{}条产品详情图片数据", productImageList.size());
            yn = productImageService.addList(productImageList);
            if (yn) {
                logger.info("产品详情图片添加成功！");
            } else {
                logger.warn("产品详情图片添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        logger.info("产品信息及其子信息添加成功！");
        jsonObject.put("success", true);
        jsonObject.put("product_id", product_id);

        return jsonObject.toJSONString();
    }

    //更新产品信息-ajax
    @ApiOperation(value = "更新产品信息")
    @ResponseBody
    @RequestMapping(value = "admin/product/{product_id}", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public String updateProduct(@RequestParam String product_name/* 产品名称 */,
                                @RequestParam String product_title/* 产品标题 */,
                                @RequestParam Integer product_category_id/* 产品类型ID */,
                                @RequestParam Double product_sale_price/* 产品最低价 */,
                                @RequestParam Double product_price/* 产品最高价 */,
                                @RequestParam Byte product_isEnabled/* 产品状态 */,
                                @RequestParam String propertyAddJson/* 产品添加属性JSON */,
                                @RequestParam String propertyUpdateJson/* 产品更新属性JSON */,
                                @RequestParam(required = false) Integer[] propertyDeleteList/* 产品删除属性ID数组 */,
                                @RequestParam(required = false) String[] productSingleImageList/*产品预览图片名称数组*/,
                                @RequestParam(required = false) String[] productDetailsImageList/*产品详情图片名称数组*/,
                                @PathVariable("product_id") Integer product_id/* 产品ID */) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合产品信息");
        Product product = new Product()
                .setProduct_id(product_id)
                .setProduct_name(product_name)
                .setProduct_title(product_title)
                .setProduct_category(new Category().setCategory_id(product_category_id))
                .setProduct_sale_price(product_sale_price)
                .setProduct_price(product_price)
                .setProduct_isEnabled(product_isEnabled)
                .setProduct_create_date(new Date());
        logger.info("更新产品信息，产品ID值为：{}", product_id);
        boolean yn = productService.update(product);
        if (!yn) {
            logger.info("产品信息更新失败！事务回滚");
            jsonObject.put("success", false);
            throw new RuntimeException();
        }
        logger.info("产品信息更新成功！");

        JSONObject object = JSON.parseObject(propertyAddJson);
        Set<String> propertyIdSet = object.keySet();
        if (propertyIdSet.size() > 0) {
            logger.info("整合产品子信息-需要添加的产品属性");
            List<PropertyValue> propertyValueList = new ArrayList<>(5);
            for (String key : propertyIdSet) {
                String value = object.getString(key);
                PropertyValue propertyValue = new PropertyValue()
                        .setPropertyValue_value(value)
                        .setPropertyValue_property(new Property().setProperty_id(Integer.valueOf(key)))
                        .setPropertyValue_product(product);
                propertyValueList.add(propertyValue);
            }
            logger.info("共有{}条需要添加的产品属性数据", propertyValueList.size());
            yn = propertyValueService.addList(propertyValueList);
            if (yn) {
                logger.info("产品属性添加成功！");
            } else {
                logger.warn("产品属性添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }

        object = JSON.parseObject(propertyUpdateJson);
        propertyIdSet = object.keySet();
        if (propertyIdSet.size() > 0) {
            logger.info("整合产品子信息-需要更新的产品属性");
            List<PropertyValue> propertyValueList = new ArrayList<>(5);
            for (String key : propertyIdSet) {
                String value = object.getString(key);
                PropertyValue propertyValue = new PropertyValue()
                        .setPropertyValue_value(value)
                        .setPropertyValue_id(Integer.valueOf(key));
                propertyValueList.add(propertyValue);
            }
            logger.info("共有{}条需要更新的产品属性数据", propertyValueList.size());
            for (int i = 0; i < propertyValueList.size(); i++) {
                logger.info("正在更新第{}条，共{}条", i + 1, propertyValueList.size());
                yn = propertyValueService.update(propertyValueList.get(i));
                if (yn) {
                    logger.info("产品属性更新成功！");
                } else {
                    logger.warn("产品属性更新失败！事务回滚");
                    jsonObject.put("success", false);
                    throw new RuntimeException();
                }
            }
        }
        if (propertyDeleteList != null && propertyDeleteList.length > 0) {
            logger.info("整合产品子信息-需要删除的产品属性");
            logger.info("共有{}条需要删除的产品属性数据", propertyDeleteList.length);
            yn = propertyValueService.deleteList(propertyDeleteList);
            if (yn) {
                logger.info("产品属性删除成功！");
            } else {
                logger.warn("产品属性删除失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        if (productSingleImageList != null && productSingleImageList.length > 0) {
            logger.info("整合产品子信息-产品预览图片");
            List<ProductImage> productImageList = new ArrayList<>(5);
            for (String imageName : productSingleImageList) {
                productImageList.add(new ProductImage()
                        .setProductImage_type((byte) 0)
                        .setProductImage_src(imageName.substring(imageName.lastIndexOf("/") + 1))
                        .setProductImage_product(product)
                );
            }
            logger.info("共有{}条产品预览图片数据", productImageList.size());
            yn = productImageService.addList(productImageList);
            if (yn) {
                logger.info("产品预览图片添加成功！");
            } else {
                logger.warn("产品预览图片添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        if (productDetailsImageList != null && productDetailsImageList.length > 0) {
            logger.info("整合产品子信息-产品详情图片");
            List<ProductImage> productImageList = new ArrayList<>(5);
            for (String imageName : productDetailsImageList) {
                productImageList.add(new ProductImage()
                        .setProductImage_type((byte) 1)
                        .setProductImage_src(imageName.substring(imageName.lastIndexOf("/") + 1))
                        .setProductImage_product(product)
                );
            }
            logger.info("共有{}条产品详情图片数据", productImageList.size());
            yn = productImageService.addList(productImageList);
            if (yn) {
                logger.info("产品详情图片添加成功！");
            } else {
                logger.warn("产品详情图片添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        jsonObject.put("success", true);
        jsonObject.put("product_id", product_id);

        return jsonObject.toJSONString();
    }

    //按ID删除产品图片并返回最新结果-ajax
    @ApiOperation(value = "按ID删除产品图片并返回最新结果")
    @ResponseBody
    @RequestMapping(value = "admin/productImage/{productImage_id}",method = RequestMethod.DELETE,produces = "application/json;charset=utf-8")
    public String deleteProductImageById(@PathVariable Integer productImage_id/* 产品图片ID */){
        JSONObject object = new JSONObject();
        logger.info("获取productImage_id为{}的产品图片信息",productImage_id);
        ProductImage productImage = productImageService.get(productImage_id);

        logger.info("删除产品图片");
        Boolean yn = productImageService.deleteList(new Integer[]{productImage_id});
        if (yn) {
            logger.info("删除图片成功！");
            object.put("success", true);
        } else {
            logger.warn("删除图片失败！事务回滚");
            object.put("success", false);
            throw new RuntimeException();
        }
        return object.toJSONString();
    }

    //上传产品图片-ajax
    @ApiOperation(value = "上传产品图片")
    @ResponseBody
    @RequestMapping(value = "admin/uploadProductImage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String uploadProductImage(@RequestParam MultipartFile file, @RequestParam String imageType, HttpSession session) {
        String originalFileName = file.getOriginalFilename();
        logger.info("获取图片原始文件名：{}", originalFileName);
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String filePath;
        String fileName = UUID.randomUUID() + extension;
        if ("single".equals(imageType)) {
            filePath = session.getServletContext().getRealPath("/") + "res/images/item/productSinglePicture/" + fileName;
        } else {
            filePath = session.getServletContext().getRealPath("/") + "res/images/item/productDetailsPicture/" + fileName;
        }

        logger.info("文件上传路径：{}", filePath);
        JSONObject object = new JSONObject();
        try {
            logger.info("文件上传中...");
            file.transferTo(new File(filePath));
            logger.info("文件上传完成");
            object.put("success", true);
            object.put("fileName", fileName);
        } catch (IOException e) {
            logger.warn("文件上传失败！");
            e.printStackTrace();
            object.put("success", false);
        }
        return object.toJSONString();
    }

    //按类型ID查询属性-ajax
    @ApiOperation(value = "按类型ID查询属性")
    @ResponseBody
    @RequestMapping(value = "admin/property/type/{property_category_id}", method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    public String getPropertyByCategoryId(@PathVariable Integer property_category_id/* 属性所属类型ID*/){
        //封装查询条件
        Category category = new Category()
                .setCategory_id(property_category_id);

        JSONObject object = new JSONObject();
        logger.info("按类型获取属性列表，类型ID：{}",property_category_id);
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(category),null);
        object.put("propertyList", JSONArray.parseArray(JSON.toJSONString(propertyList)));

        return object.toJSONString();
    }


































    @PostMapping(value = "admin/getCategoryList")
    @ApiOperation(value = "获取产品类型")
    @ResponseBody
    public ListResp<List<CategoryDomain>> getCategoryList(){
        List<Category> list = categoryService.getList(null, null);
        List<CategoryDomain> domainList=new ArrayList<>();
        for (Category category:list) {
            CategoryDomain domain = new CategoryDomain();
            BeanUtils.copyProperties(category,domain);
            domainList.add(domain);
        }
        ListResp<List<CategoryDomain>> data = new ListResp<>();
        data.setData(domainList);
        return data;
    }
    @PostMapping(value = "admin/getPropertyList")
    @ApiOperation(value = "根据类型id类型获取属性")
    @ResponseBody
    public ListResp<List<PropertyDomain>> getPropertyList(int category_id){
        Category category = categoryService.get(category_id);
        List<PropertyDomain> domainList=new ArrayList<PropertyDomain>();
        List<Property> list = propertyService.getList(new Property().setProperty_category(category), null);
        for (Property property:list) {
            PropertyDomain domain = new PropertyDomain();
            BeanUtils.copyProperties(property,domain);
            domainList.add(domain);
        }
        ListResp<List<PropertyDomain>> data = new ListResp<>();
        data.setData(domainList);
        return data;
    }

}
