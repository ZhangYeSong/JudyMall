package com.judy.mmall.service;

import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse setCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getCategory(Integer categoryId);

    ServerResponse<List<Integer>> recurseCategory(Integer categoryId);
}
