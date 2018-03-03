package com.judy.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.dao.CategoryMapper;
import com.judy.mmall.pojo.Category;
import com.judy.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger("UserServiceImpl");

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (null == parentId || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加分类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加分类成功");
        } else {
            return ServerResponse.createByErrorMessage("添加分类失败");
        }
    }

    @Override
    public ServerResponse setCategoryName(Integer categoryId, String categoryName) {
        if (null == categoryId || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加分类参数错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新分类名称成功");
        } else {
            return ServerResponse.createByErrorMessage("更新分类名称失败");
        }
    }

    @Override
    public ServerResponse<List<Category>> getCategory(Integer categoryId) {
        List<Category> categories = categoryMapper.getCategory(categoryId);
        if (CollectionUtils.isEmpty(categories)) {
            logger.info("当前分类节点没有子分类节点");
        }
        return ServerResponse.createBySuccess(categories);
    }

    @Override
    public ServerResponse recurseCategory(Integer categoryId) {
        HashSet<Category> categories = Sets.newHashSet();
        ArrayList<Category> categoryArrayList = Lists.newArrayList();
        findAllChildCategories(categories, categoryId);
        categoryArrayList.addAll(categories);
        return ServerResponse.createBySuccess(categoryArrayList);
    }

    private Set<Category> findAllChildCategories(Set<Category> categories, Integer categoryId) {
        Category resultCategory = categoryMapper.selectByPrimaryKey(categoryId);
        if(resultCategory != null){
            categories.add(resultCategory);
        }
        List<Category> resultCategories = categoryMapper.getCategory(categoryId);
        for (Category category : resultCategories) {
            findAllChildCategories(categories, category.getId());
        }
        return categories;
    }
}
