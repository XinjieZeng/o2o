package com.example.o2o.web.shopadmin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/o2o")
public class ShopAdminController {
    @RequestMapping(path = "/shopadmin", method = RequestMethod.GET)
    public String shopOperation() {
        return "shop/shopoperation";
    }

}
