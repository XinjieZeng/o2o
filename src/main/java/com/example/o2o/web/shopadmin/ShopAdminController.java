package com.example.o2o.web.shopadmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/o2o")
public class ShopAdminController {
    @GetMapping("/shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";
    }

    @GetMapping("/shoplist")
    public String shopList() {
        return "shop/shoplist";
    }

    @GetMapping("/shopmanagement")
    public String shopManagement() {
        return "shop/shopmanagement";
    }

    @GetMapping("/productcategorymanagement")
    public String productManagement() {
        return "shop/productcategorymanagement";
    }

    @RequestMapping("/productoperation")
    public String productOperation() {
        return "shop/productoperation";
    }

}
