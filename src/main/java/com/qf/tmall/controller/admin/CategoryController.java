package com.qf.tmall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qf.tmall.controller.BaseController;
import com.qf.tmall.entity.Category;
import com.qf.tmall.entity.Page;
import com.qf.tmall.entity.Property;
import com.qf.tmall.entity.resp.CategoryResp;
import com.qf.tmall.entity.resp.ResponseEntity;
import com.qf.tmall.service.CategoryService;
import com.qf.tmall.service.LastIDService;
import com.qf.tmall.service.PropertyService;
import com.qf.tmall.util.PageUtil;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 后台管理-分类页
 */
@Api(tags = "后台管理-分类页")
@Controller
public class CategoryController extends BaseController {
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "lastIDService")
    private LastIDService lastIDService;
    @Resource(name = "propertyService")
    private PropertyService propertyService;

    //转到后台管理-分类页-ajax
    @RequestMapping(value = "admin/category", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }

        logger.info("获取前10条分类列表");
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Category> categoryList = categoryService.getList(null, pageUtil);
        map.put("categoryList", categoryList);
        logger.info("获取分类总数量");
        Integer categoryCount = categoryService.getTotal(null);
        map.put("categoryCount", categoryCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(categoryCount);
        map.put("pageUtil", pageUtil);

        logger.info("转到后台管理-分类页-ajax方式");
        return "admin/categoryManagePage";
    }


    @ApiOperation(value = "获取商品列表数据", notes = "自定义请求头sessionId，sessionId的值是登陆接口返回的")
    @RequestMapping(value = "/category/list", method = RequestMethod.POST)
    @ResponseBody
    //ResponseBody封包,@RequestBody解包，自定义协议、二进制字符串
    //返回一个类型为
    public CategoryResp<List<Category>> getCategoryList(@RequestBody Page page) {
        CategoryResp<List<Category>> data = new CategoryResp<List<Category>>();
        System.out.println(page.getSearch());
//        RequestAssets.assetsError(errors);
        logger.info("检查管理员权限");
        logger.info("获取分类列表");
        PageUtil pageUtil = new PageUtil(page.getPageNum()-1, page.getPageSize());
        List<Category> list = new ArrayList<Category>();
        List<Category> categoryList = categoryService.getList(page.getSearch(),pageUtil);
        if (categoryList != null) {
            System.out.println(list);
            for (Category category : categoryList) {
                Category bean = new Category();
                try {
                    BeanUtils.copyProperties(category, bean);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                list.add(bean);
            }
        }
        data.setData(list);
        logger.info("获取类别总数量");
        Integer productCount = categoryService.getTotal(null);
        pageUtil.setTotal(productCount);
        data.setPageUtil(pageUtil);
        logger.info("转到后台管理-类别页-ajax方式");
        return data;
    }
    //转到后台管理-分类详情页-ajax
    @RequestMapping(value = "admin/category/{cid}", method = RequestMethod.GET)
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer cid/* 分类ID */) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        logger.info("获取category_id为{}的分类信息", cid);
        Category category = categoryService.get(cid);
        logger.info("获取分类子信息-属性列表");
        category.setPropertyList(propertyService.getList(new Property().setProperty_category(category), null));
        map.put("category", category);

        logger.info("转到后台管理-分类详情页-ajax方式");
        return "admin/include/categoryDetails";
    }

    //转到后台管理-分类添加页-ajax
    @RequestMapping(value = "admin/category/new", method = RequestMethod.GET)
    public String goToAddPage(HttpSession session, Map<String, Object> map) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }

        logger.info("转到后台管理-分类添加页-ajax方式");
        return "admin/include/categoryDetails";
    }

    //添加分类信息-ajax
    @ResponseBody
    @RequestMapping(value = "admin/category", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addCategory(@RequestParam String category_name/* 分类名称 */,
                              @RequestParam String category_image_src/* 分类图片路径 */) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合分类信息");
        Category category = new Category()
                .setCategory_name(category_name)
                .setCategory_image_src(category_image_src.substring(category_image_src.lastIndexOf("/") + 1));
        logger.info("添加分类信息");
        boolean yn = categoryService.add(category);
        if (yn) {
            int category_id = lastIDService.selectLastID();
            logger.info("添加成功！,新增分类的ID值为：{}", category_id);
            jsonObject.put("success", true);
            jsonObject.put("category_id", category_id);
        } else {
            jsonObject.put("success", false);
            logger.warn("添加失败！事务回滚");
            throw new RuntimeException();
        }

        return jsonObject.toJSONString();
    }

    //更新分类信息-ajax
    @ResponseBody
    @RequestMapping(value = "admin/category/{category_id}", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public String updateCategory(@RequestParam String category_name/* 分类名称 */,
                                 @RequestParam String category_image_src/* 分类图片路径 */,
                                 @PathVariable("category_id") Integer category_id/* 分类ID */) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合分类信息");
        Category category = new Category()
                .setCategory_id(category_id)
                .setCategory_name(category_name)
                .setCategory_image_src(category_image_src.substring(category_image_src.lastIndexOf("/") + 1));
        logger.info("更新分类信息，分类ID值为：{}", category_id);
        boolean yn = categoryService.update(category);
        if (yn) {
            logger.info("更新成功！");
            jsonObject.put("success", true);
            jsonObject.put("category_id", category_id);
        } else {
            jsonObject.put("success", false);
            logger.info("更新失败！事务回滚");
            throw new RuntimeException();
        }

        return jsonObject.toJSONString();
    }


    // 上传分类图片-ajax
    @ResponseBody
    @RequestMapping(value = "admin/uploadCategoryImage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String uploadCategoryImage(@RequestParam MultipartFile file, HttpSession session) {
        String originalFileName = file.getOriginalFilename();
        logger.info("获取图片原始文件名:  {}", originalFileName);
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;
        String filePath = session.getServletContext().getRealPath("/") + "res/images/item/categoryPicture/" + fileName;

        logger.info("文件上传路径：{}", filePath);
        JSONObject object = new JSONObject();
        try {
            logger.info("文件上传中...");
            file.transferTo(new File(filePath));
            logger.info("文件上传完成");
            object.put("success", true);
            object.put("fileName", fileName);
        } catch (IOException e) {
            logger.warn("文件上传失败!");
            e.printStackTrace();
            object.put("success", false);
        }

        return object.toJSONString();
    }





































    @ApiOperation(value = "删除商品数据", notes = "自定义请求头sessionId，sessionId的值是登陆接口返回的")
    @RequestMapping(value = "/category/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity deleteCategory(@RequestBody Category requesCategory){
        Category category = categoryService.get(requesCategory.getCategory_id());
        System.out.println("操作内容："+category.getCategory_name()+".setflog = 0");
        boolean update = categoryService.update(category);
        if (update){
            logger.info("删除成功！");
            return new ResponseEntity("1","删除成功");
        }else {
            logger.info("删除失败！事务回滚");
            throw new RuntimeException();
        }
    }

    @ApiOperation(value = "修改商品数据", notes = "自定义请求头sessionId，sessionId的值是登陆接口返回的")
    @RequestMapping(value = "/category/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity updateCategory(@RequestBody Category category){
        logger.info("更新分类信息，分类ID值为：{}", category.getCategory_id());
        boolean yn = categoryService.update(category);
        if (yn) {
            logger.info("更新成功！");
            return new ResponseEntity("1","更新成功");
        } else {
            logger.info("更新失败！事务回滚");
            throw new RuntimeException();
//            return new ResponseEntity("0","更新失败，事务回滚");
        }
    }

    @ApiOperation(value = "添加商品数据", notes = "自定义请求头sessionId，sessionId的值是登陆接口返回的")
    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addCategory(@RequestBody Category category){
        logger.info("添加分类信息");
        category.setCategory_image_src(category.getCategory_image_src().substring(category.getCategory_image_src().lastIndexOf("/") + 1));
        boolean yn = categoryService.add(category);
        ResponseEntity responseEntity = new ResponseEntity();
        if (yn) {
            String category_id = String.valueOf(lastIDService.selectLastID());
            logger.info("添加对象： Category");
            logger.info("Category_id：" + category_id);
            logger.info("Category_name: " + category.getCategory_name());
            logger.info("Category_image_src: " + category.getCategory_image_src());
            return new ResponseEntity("1","添加成功",category_id);
        } else {
            logger.info("添加失败！事务回滚");
            throw new RuntimeException();
//            return new ResponseEntity("0","更新失败，事务回滚");
        }
    }

    @RequestMapping(value = "Category/Details", method = RequestMethod.GET)
    public String localtionDetails(HttpSession session){
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        logger.info("进入添加页面");
        return "admin/include/categoryDetails";
    }

    // 图片上传
    @ResponseBody
    @RequestMapping(value = "admin/imageUpload", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ResponseEntity imgUpload(@RequestParam MultipartFile file, HttpSession session) {
        String originalFileName = file.getOriginalFilename();
        logger.info("获取图片原始文件名:  {}", originalFileName);
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;
        String filePath = session.getServletContext().getRealPath("/") + "res/images/item/categoryPicture/" + fileName;

        logger.info("文件上传路径：{}", filePath);
        ResponseEntity responseEntity = new ResponseEntity();
        try {
            logger.info("文件上传中...");
            file.transferTo(new File(filePath));
            logger.info("文件上传完成");
            responseEntity.setSuccess("1");
            responseEntity.setMsg("图片上传成功");
            responseEntity.setFileName(fileName);
        } catch (IOException e) {
            logger.warn("文件上传失败!");
            e.printStackTrace();
            responseEntity.setSuccess("0");
            responseEntity.setMsg("图片上传失败");
        }

        return responseEntity;
    }






}