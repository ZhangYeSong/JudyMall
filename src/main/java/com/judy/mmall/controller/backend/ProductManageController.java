package com.judy.mmall.controller.backend;

import com.judy.mmall.commom.ResponseCode;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.Product;
import com.judy.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("save_or_update_product.do")
    @ResponseBody
    public ServerResponse saveOrUpdateProduct(HttpSession session, Product product) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }

        return iProductService.saveOrUpdateProduct(product);
    }

    @RequestMapping("set_product_state.do")
    @ResponseBody
    public ServerResponse setProductState(HttpSession session, Integer productId, Integer productState) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }

        return iProductService.setProductState(productId, productState);
    }
}
