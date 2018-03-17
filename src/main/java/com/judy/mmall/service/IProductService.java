package com.judy.mmall.service;

import com.github.pagehelper.PageInfo;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.Product;
import com.judy.mmall.vo.ProductDetailVo;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setProductState(Integer productId, Integer productState);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProductList(String productName, Integer productId, int pageNum, int pageSize);
}
