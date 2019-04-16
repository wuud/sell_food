package cn.sell.product.controller;

import cn.sell.product.VO.ProductInfoVO;
import cn.sell.product.VO.ProductVO;
import cn.sell.product.VO.ResultVO;
import cn.sell.product.dto.CartDTO;
import cn.sell.product.model.ProductCategory;
import cn.sell.product.model.ProductInfo;
import cn.sell.product.service.ProductCategoryService;
import cn.sell.product.service.ProductInfoService;
import cn.sell.product.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    ProductInfoService productInfoService;

    /**
     * 1. 查询所有在架的商品
     * 2. 获取类目type列表
     * 3. 查询类目
     * 4. 构造数据
     */
    @RequestMapping(value = "/list")
    public ResultVO result() {
        //1. 查询所有在架的商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        //2. 获取类目type列表
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());
        //3. 查询类目
        List<ProductCategory> productCategoryList = productCategoryService.getByTypeIn(categoryTypeList);
        //4. 构造数据
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productVO.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }


        return ResultVOUtil.success(productVOList);
    }

    @PostMapping(value = "/listForOrder")
    public List<ProductInfo> getListForOrder(@RequestBody List<String> productidList) {
        try {
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return productInfoService.findByProductIdIn(productidList);
    }

    @PostMapping(value = "/decrStock")
    public void decrProductStock(@RequestBody List<CartDTO> cartDTOList) {
        productInfoService.decrProductStock(cartDTOList);
    }

}
