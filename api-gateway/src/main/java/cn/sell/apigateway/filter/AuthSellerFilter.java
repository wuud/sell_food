package cn.sell.apigateway.filter;

import cn.sell.apigateway.constant.CookieConstant;
import cn.sell.apigateway.constant.RedisConstant;
import cn.sell.apigateway.utils.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class AuthSellerFilter extends ZuulFilter {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    /**
     * /order/order/finish 只能由卖家访问  根据token获得用户
     */
    @Override
    public boolean shouldFilter() {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if(request.getRequestURI().equals("/order/order/finish")){
            return true;
        }
        return false;
    }

    /**
     * /order/order/finish 只能由卖家访问  根据token获得用户
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        Cookie cookie= CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie==null || StringUtils.isEmpty(cookie.getValue())
                        || StringUtils.isEmpty(""+redisTemplate.opsForValue().
                get(String.format(RedisConstant.TOKEN_TEMPLATE,cookie.getValue())))){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
