package com.judy.mmall.controller.portal;

import com.judy.mmall.commom.ResponseCode;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.service.IProductService;
import com.judy.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    IProductService iProductService;

    @RequestMapping("get_product_detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> getProductDetail(HttpSession session, Integer productId, Integer productState) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }

        return iProductService.getProductDetail(productId);
    }


}
