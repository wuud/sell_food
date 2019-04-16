package cn.sell.product.service;

import cn.sell.product.common.ProductInfoOutput;
import cn.sell.product.dao.ProductInfoDao;
import cn.sell.product.dto.CartDTO;
import cn.sell.product.enums.ProductErrorEnum;
import cn.sell.product.enums.ProductStatus;
import cn.sell.product.exception.ProductException;
import cn.sell.product.model.ProductInfo;
import cn.sell.product.utils.JsonUtil;
import com.netflix.discovery.converters.Auto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductInfoService {

    @Autowired
    ProductInfoDao productInfoDao;
    @Autowired
    AmqpTemplate amqpTemplate;

    private static String QUEUE = "queue";

    public ProductInfo getById(String id) {
        Optional<ProductInfo> op = productInfoDao.findById(id);
        return op.get();
    }

    public void add(ProductInfo pi) {
        productInfoDao.save(pi);
    }

    public void update(ProductInfo pi) {
        productInfoDao.save(pi);
    }

    /**
     * 返回所有正在上架的商品
     */
    public List<ProductInfo> findUpAll() {
        return productInfoDao.findByProductStatus(ProductStatus.UP.getCode());
    }

    /**
     * 返回分页的所有商品
     */
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoDao.findAll(pageable);
    }

    /**
     * 根据商品id返回对应的商品列表
     *
     * @param list
     * @return
     */
    public List<ProductInfo> findByProductIdIn(List<String> list) {
        return productInfoDao.findByProductIdIn(list);
    }

    /**
     * 减去对应商品的库存
     *
     * @param cartDTOList
     */
    public void decrProductStock(List<CartDTO> cartDTOList) {
        List<ProductInfo> productInfoList = decrStock(cartDTOList);

        List<ProductInfoOutput> productInfoOutputList = productInfoList.stream().map(e -> {
            ProductInfoOutput productInfoOutput = new ProductInfoOutput();
            BeanUtils.copyProperties(e, productInfoOutput);
            return productInfoOutput;
        }).collect(Collectors.toList());

        // 操作执行成功后向order服务发送一条消息。
        amqpTemplate.convertAndSend("productInfo", JsonUtil.toJson(productInfoOutputList));

    }

    @Transactional
    public List<ProductInfo> decrStock(List<CartDTO> cartDTOList) {
        List<ProductInfo> productInfoList = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOList) {
            Optional<ProductInfo> productInfoOptional = productInfoDao.findById(cartDTO.getProductId());
            if (!productInfoOptional.isPresent()) {
                throw new ProductException(ProductErrorEnum.PRODUCT_NOT_EXIST);
            }
            ProductInfo productInfo = productInfoOptional.get();
            int result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new ProductException(ProductErrorEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            productInfoDao.save(productInfo);
            productInfoList.add(productInfo);
        }
        return productInfoList;
    }
}
