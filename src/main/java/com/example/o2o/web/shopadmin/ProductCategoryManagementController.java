package com.example.o2o.web.shopadmin;

import com.example.o2o.dto.ProductCategoryExecution;
import com.example.o2o.dto.Result;
import com.example.o2o.entity.ProductCategory;
import com.example.o2o.entity.Shop;
import com.example.o2o.enums.ProductCategoryStateEnum;
import com.example.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("o2o/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;


    @GetMapping("/getproductcategorylist")
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

//        Shop shop = new Shop();
//        shop.setShopId(28L);
//        request.getSession().setAttribute("currentShop", shop);

        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");


            if (currentShop != null && currentShop.getShopId() > 0) {
                List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
                return new Result<List<ProductCategory>>(true, productCategoryList);
            }


            return new Result<List<ProductCategory>>(false, ProductCategoryStateEnum.INNER_ERROR.getState(), ProductCategoryStateEnum.INNER_ERROR.getStateInfo());
    }

    @PostMapping("/addproductcategories")
    @ResponseBody
    private Map<String, Object> addProductCategories(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setShopId(currentShop.getShopId());
        }

        if (productCategoryList != null && !productCategoryList.isEmpty()) {
            ProductCategoryExecution productCategoryExecution = productCategoryService.batchInsertProductCategory(productCategoryList);
            if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                return modelMap;
            }

            modelMap.put("success", false);
            modelMap.put("errMsg", productCategoryExecution.getStateInfo());
            return modelMap;
         }

        modelMap.put("success", false);
        modelMap.put("errMsg", "please enter one category");
        return modelMap;
    }

    @PostMapping("removeproductcategory")
    @ResponseBody
    private Map<String, Object> removeProductCategories(Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();


        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        if (productCategoryId != null && productCategoryId > 0) {
            ProductCategoryExecution productCategoryExecution = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());

            if (productCategoryExecution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                return modelMap;
            }

            modelMap.put("success", false);
            modelMap.put("errMsg", productCategoryExecution.getStateInfo());
            return modelMap;
        }

        modelMap.put("success", false);
        modelMap.put("errMsg", "please choose at least one category");
        return modelMap;
    }
}
