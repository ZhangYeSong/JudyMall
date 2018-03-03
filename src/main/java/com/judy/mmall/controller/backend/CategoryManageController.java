package com.judy.mmall.controller.backend;

import com.judy.mmall.commom.ResponseCode;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.Category;
import com.judy.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",
            defaultValue = "0") Integer parentId) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }

        return iCategoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }
        return iCategoryService.setCategoryName(categoryId, categoryName);
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse<List<Category>> getCategory(HttpSession session, @RequestParam(value = "categoryId",
            defaultValue = "0") Integer categoryId) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }
        return iCategoryService.getCategory(categoryId);
    }

    @RequestMapping("recurse_category.do")
    @ResponseBody
    public ServerResponse recurseCategory(HttpSession session, @RequestParam(value = "categoryId",
            defaultValue = "0") Integer categoryId) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }
        return iCategoryService.recurseCategory(categoryId);
    }
}
