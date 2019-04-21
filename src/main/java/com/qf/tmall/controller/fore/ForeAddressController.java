package com.qf.tmall.controller.fore;

import com.alibaba.fastjson.JSONObject;
import com.qf.tmall.controller.BaseController;
import com.qf.tmall.entity.Address;
import com.qf.tmall.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ACI
 * @Title: ForeAddressController
 * @ProjectName com.qf
 * @Description: TODO
 * @date 2019/2/20 16:08
 */
@Api(tags = "前台天猫-获取用户地址")
@Controller
public class ForeAddressController extends BaseController {
    @Resource(name = "addressService")
    private AddressService addressService;

    //根据address_areaId获取地址信息-ajax
    @ApiOperation(value = "根据address_areaId获取地址信息")
    @ResponseBody
    @RequestMapping(value = "address/{areaId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    protected String getAddressByAreaId(@PathVariable String areaId) {
        JSONObject object = new JSONObject();
        logger.info("获取AreaId为{}的地址信息");
        List<Address> addressList = addressService.getList(null, areaId);
        if (addressList == null || addressList.size() <= 0) {
            object.put("success", false);
            return object.toJSONString();
        }
        logger.info("获取该地址可能的子地址信息");
        List<Address> childAddressList = addressService.getList(null, addressList.get(0).getAddress_areaId());
        object.put("success", true);
        object.put("addressList", addressList);
        object.put("childAddressList", childAddressList);
        return object.toJSONString();
    }
}

