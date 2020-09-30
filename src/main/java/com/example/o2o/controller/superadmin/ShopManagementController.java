package com.example.o2o.controller.superadmin;

import com.example.o2o.entity.Shop;
import com.example.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @RequestMapping(path = "/addshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addShop(HttpServletRequest request) {
        // receive and convert parameters inlcuding shop information
        // register shops
        // return result

        Map<String, Object> modelMap = new HashMap<>();
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");

        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (JsonProcessingException e) {
            modelMap.put("success: ", false);
            modelMap.put("errMsg: ", e.getMessage());
            return modelMap;
        }

       return modelMap;

    }
}
