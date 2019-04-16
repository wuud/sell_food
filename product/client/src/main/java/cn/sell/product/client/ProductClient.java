package cn.sell.product.client;

import cn.sell.product.common.DecreaseStockInput;
import cn.sell.product.common.ProductInfoOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "product")
public interface ProductClient {

    @RequestMapping("/test")
    public String test();

    @PostMapping(value = "/product/listForOrder")
    public List<ProductInfoOutput> getListForOrder(@RequestBody List<String> productidList);

    @PostMapping(value = "/product/decrStock")
    public void decrProductStock(@RequestBody List<DecreaseStockInput> cartDTOList);
}
