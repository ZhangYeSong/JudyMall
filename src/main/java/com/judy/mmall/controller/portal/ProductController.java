package com.judy.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.service.IProductService;
import com.judy.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("get_product_list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "orderBy", defaultValue = "10") String orderBy) {
        return iProductService.getProductList(keyword,categoryId, pageNum, pageSize, orderBy);
    }
}
