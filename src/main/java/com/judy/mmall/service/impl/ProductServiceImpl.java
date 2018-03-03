package com.judy.mmall.service.impl;

import com.judy.mmall.commom.ResponseCode;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.dao.ProductMapper;
import com.judy.mmall.pojo.Product;
import com.judy.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

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
}
