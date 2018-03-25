package com.judy.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.judy.mmall.commom.ResponseCode;
import com.judy.mmall.commom.ServerResponse;
import com.judy.mmall.pojo.Product;
import com.judy.mmall.service.IFileService;
import com.judy.mmall.service.IProductService;
import com.judy.mmall.util.PropertiesUtil;
import com.judy.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

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

    @RequestMapping("manage_product_detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> manageProductDetail(HttpSession session, Integer productId, Integer productState) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }

        return iProductService.manageProductDetail(productId);
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

    @RequestMapping("upload_image.do")
    @ResponseBody
    public ServerResponse uploadImage(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        if (!ServerResponse.isManager(session)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有以管理员身份登录");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.uploadFile(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }
}
