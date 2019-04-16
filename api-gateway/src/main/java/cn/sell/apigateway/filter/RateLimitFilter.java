package cn.sell.apigateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 *  Zuul实现限流
 * 令牌桶算法：
 *      令牌桶算法的原理是系统会以一个恒定的速度往桶里放入令牌，
 *      而如果请求需要被处理，则需要先从桶里获取一个令牌，
 *      当桶里没有令牌可取时，则拒绝服务。
 */
@Component
public class RateLimitFilter extends ZuulFilter {

    // 参数：每秒钟向桶中放入多少令牌
    private static final RateLimiter RETA_LIMITER=RateLimiter.create(100);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 要比优先级最高的filter优先级还要高
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取令牌失败就抛一个异常
        if(!RETA_LIMITER.tryAcquire()){
           throw new RuntimeException();
        }

        return null;
    }
}
