package com.judy.mmall.service;

import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.Product;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setProductState(Integer productId, Integer productState);
}
