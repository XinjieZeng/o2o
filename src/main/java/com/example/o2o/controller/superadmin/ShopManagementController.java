package com.example.o2o.controller.superadmin;

import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.PersonInfo;
import com.example.o2o.entity.Shop;
import com.example.o2o.enums.ShopStateEnum;
import com.example.o2o.service.ShopService;
import com.example.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    private final ShopService shopService;

    @Autowired
    public ShopManagementController(ShopService shopService) {
        this.shopService = shopService;
    }

    @RequestMapping(path = "/addshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addShop(HttpServletRequest request) {
        // receive shop information and convert them into Shop class
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

        // store the uploaded image stream
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "the image can not be empty");
            return modelMap;
        }

        //add shop
        if (shop != null && shopImg != null) {
            PersonInfo owner = new PersonInfo();
            //TODO SESSION
            owner.setUserId(2L);
            shop.setOwner(owner);

            ShopExecution shopExecution = null;
            try {
                shopExecution = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                }
                else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }

            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }

        }
        else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "please enter the shop information");
        }

       return modelMap;
    }
}
