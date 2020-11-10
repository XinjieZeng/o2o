package com.example.o2o.web.shopadmin;

import com.example.o2o.dto.ImageHolder;
import com.example.o2o.dto.ProductExecution;
import com.example.o2o.dto.ProductStateEnum;
import com.example.o2o.entity.Product;
import com.example.o2o.entity.ProductCategory;
import com.example.o2o.entity.Shop;
import com.example.o2o.service.ProductCategoryService;
import com.example.o2o.service.ProductService;
import com.example.o2o.util.CodeUtil;
import com.example.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("o2o/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    private static final int IMAGE_MAX_COUNT = 5;

    @PostMapping("/addproduct")
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //check verification code
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "the verification code is invalid");
            return modelMap;
        }

        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, productImgList);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        try {
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        if (product != null && thumbnail != null && !productImgList.isEmpty()) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);

                ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                }

                modelMap.put("success", false);
                modelMap.put("errMsg", pe.getStateInfo());

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }

        modelMap.put("success", false);
        modelMap.put("errMsg", "please enter product information");
        return modelMap;
    }

    @GetMapping("/getproductbyid")
    @ResponseBody
    private Map<String, Object> getProductById(@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<>();

        if (productId > -1) {
            Product product = productService.getProductById(productId);
            List<ProductCategory> productCategoryList = productCategoryService
                    .getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
            return modelMap;
        }

        modelMap.put("success", false);
        modelMap.put("errMsg", "empty productId");
        return modelMap;
    }

    @PostMapping("/modifyproduct")
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");

        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, productImgList);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        if (product != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                }
                modelMap.put("success", false);
                modelMap.put("errMsg", pe.getStateInfo());
                return modelMap;

            }
            catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }

        modelMap.put("success", false);
        modelMap.put("errMsg", "请输入商品信息");
        return modelMap;
    }

    private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList)
            throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }

        for (int i = 0; i < IMAGE_MAX_COUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);

            if (productImgFile == null) {
                break;
            }

            ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
                        productImgFile.getInputStream());
            productImgList.add(productImg);

        }
        return thumbnail;
    }
}
