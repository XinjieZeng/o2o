package com.example.o2o.web.shopadmin;

import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.*;
import com.example.o2o.enums.ShopStateEnum;
import com.example.o2o.service.AreaService;
import com.example.o2o.service.ProductCategoryService;
import com.example.o2o.service.ShopCategoryService;
import com.example.o2o.service.ShopService;
import com.example.o2o.util.CodeUtil;
import com.example.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("o2o/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @Autowired
    DefaultKaptcha defaultKaptcha;


    @GetMapping("/getshoplist")
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        request.getSession().setAttribute("user", user);
        user = (PersonInfo) request.getSession().getAttribute("user");

        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition);
            modelMap.put("success", true);
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("user", user);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }



    @GetMapping("/getshopmanagementinfo")
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId <= 0) {
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "o2o/shop/shoplist");
                return modelMap;
            }

            Shop currentShop = (Shop) currentShopObj;
            modelMap.put("redirect", false);
            modelMap.put("shopId", currentShop.getShopId());
            return modelMap;
        }

        Shop currentShop = new Shop();
        currentShop.setShopId(shopId);
        request.getSession().setAttribute("currentShop", currentShop);
        return modelMap;
    }



    @GetMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            String createText = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("verificationCode", createText);
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }


    @GetMapping("/getshopinitinfo")
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();

        try {
            List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList();
            List<Area> areaList = areaService.getAreaList();
            modelMap.put("success", true);
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @GetMapping(path = "/addshop")
    @ResponseBody
    private Map<String, Object> addShop(HttpServletRequest request) {
        // receive shop information and convert them into Shop class
        Map<String, Object> modelMap = new HashMap<>();

        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "the verification is not valid");
            return modelMap;
        }

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
        MultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = multipartHttpServletRequest.getFile("shopImg");
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
            owner.setUserId(1L);
            shop.setOwner(owner);

            ShopExecution shopExecution = null;
            try {
                shopExecution = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                }
                modelMap.put("success", false);
                modelMap.put("errMsg", shopExecution.getStateInfo());
                return modelMap;

            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }

        }

        modelMap.put("success", false);
        modelMap.put("errMsg", "please enter the shop information");
        return modelMap;
    }

    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");

        if (shopId > -1) {
            try {
                Shop shop = shopService.getShopById(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
            return modelMap;
        }

        modelMap.put("success", false);
        modelMap.put("errMsg", "empty shopId");
        return modelMap;
    }


    @GetMapping(value = "/modifyshop")
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "invalid verification code");
            return modelMap;
        }
        // 1.convert shop string to shop object
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //receive shop image
        MultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg =  multipartHttpServletRequest.getFile("shopImg");
        }
        // 2.update shop information
        if (shop != null && shop.getShopId() != null) {
            PersonInfo owner = new PersonInfo();
            //TODO SESSION
            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExecution se;
            try {
                if (shopImg == null) {
                    se = shopService.modifyShop(shop, null, null);
                } else {
                   se = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }

                if (se.getState() == ShopStateEnum.SUCCESSFUL.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                }

                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
                return modelMap;

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }

        modelMap.put("success", false);
        modelMap.put("errMsg", "请输入店铺Id");
        return modelMap;
    }
}
