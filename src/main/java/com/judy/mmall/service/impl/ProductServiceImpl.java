package com.judy.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.judy.mmall.commom.ResponseCode;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.dao.CategoryMapper;
import com.judy.mmall.dao.ProductMapper;
import com.judy.mmall.pojo.Category;
import com.judy.mmall.pojo.Product;
import com.judy.mmall.service.IProductService;
import com.judy.mmall.util.DateTimeUtil;
import com.judy.mmall.util.PropertiesUtil;
import com.judy.mmall.vo.ProductDetailVo;
import com.judy.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (null == product) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数未空");
        }

        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImages = product.getSubImages().split(",");
            if (subImages.length > 0) {
                product.setMainImage(subImages[0]);
            }
        }

        if (product.getId() != null) {
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("更新商品成功");
            } else {
                return ServerResponse.createByErrorMessage("更新商品失败");
            }
        } else {
            int rowCount = productMapper.insert(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("添加商品成功");
            } else {
                return ServerResponse.createByErrorMessage("添加商品失败");
            }
        }
    }

    @Override
    public ServerResponse setProductState(Integer productId, Integer productState) {
        if (productId == null || productState == null) {
            ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数为空");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createBySuccessMessage("商品id不存在");
        }

        product.setStatus(productState);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("修改商品状态成功");
        } else {
            return ServerResponse.createByErrorMessage("修改商品状态失败");
        }
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createBySuccessMessage("商品id不存在");
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectProducList();
        List<ProductListVo> productVos = new ArrayList<>();
        for (Product product : products) {
            ProductListVo productListVo = assembleProductListVo(product);
            productVos.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productVos);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> searchProductList(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        productName = "%" + productName + "%";
        List<Product> products = productMapper.selectByNameAndId(productName, productId);
        List<ProductListVo> productVos = new ArrayList<>();
        for (Product product : products) {
            ProductListVo productListVo = assembleProductListVo(product);
            productVos.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productVos);
        return ServerResponse.createBySuccess(pageResult);

    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setTitle(product.getSubtitle());
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return productListVo;
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setName(product.getName());
        productDetailVo.setTitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }
}
