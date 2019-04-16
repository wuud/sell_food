package cn.sell.order.message;

import cn.sell.order.utils.JsonUtil;
import cn.sell.product.common.ProductInfoOutput;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductInfoReceiver {


    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void process(String message) {

        List<ProductInfoOutput> productInfoOutputList = (List<ProductInfoOutput>)JsonUtil.
                fromJson(message, new TypeReference<List<ProductInfoOutput>>(){});

        log.info("从【{}】队列接收到消息：{}","productInfo",productInfoOutputList);
        
    }


}
