package com.judy.mmall.service;

import com.github.pagehelper.PageInfo;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.Product;
import com.judy.mmall.vo.ProductDetailVo;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setProductState(Integer productId, Integer productState);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getAllProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProductList(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
