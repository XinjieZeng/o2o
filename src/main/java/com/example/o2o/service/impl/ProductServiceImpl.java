package com.example.o2o.service.impl;

import com.example.o2o.dao.ProductDao;
import com.example.o2o.dao.ProductImgDao;
import com.example.o2o.dto.ImageHolder;
import com.example.o2o.dto.ProductExecution;
import com.example.o2o.dto.ProductStateEnum;
import com.example.o2o.entity.Product;
import com.example.o2o.entity.ProductImg;
import com.example.o2o.service.ProductService;
import com.example.o2o.util.ImageUtil;
import com.example.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;



    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {

            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);

            if (thumbnail != null) {
                addThumbnail(product, thumbnail);
            }

            productDao.save(product);

            if (productImgList != null && productImgList.size() > 0) {
                addProductImgList(product, productImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        }

        return new ProductExecution(ProductStateEnum.EMPTY);

    }

    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();

        for (ImageHolder productImgHolder : productImgHolderList) {
            String imgAddr = ImageUtil.generateThumbnail(productImgHolder, dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }

        productImgList.forEach(image -> productImgDao.save(image));
    }

}
