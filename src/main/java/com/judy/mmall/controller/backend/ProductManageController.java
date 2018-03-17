package com.judy.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.judy.mmall.commom.ResponseCode;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.Product;
import com.judy.mmall.service.IProductService;
import com.judy.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping("get_product_detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> getProductDetail(HttpSession session, Integer productId, Integer productState) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }

        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("get_product_list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }

        return iProductService.getProductList(pageNum, pageSize);
    }

    @RequestMapping("search_product_list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }

        return iProductService.searchProductList(productName, productId, pageNum, pageSize);
    }
}
