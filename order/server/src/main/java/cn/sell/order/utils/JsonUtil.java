package cn.sell.order.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转为Json字符串
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Json字符串转为对象
     * @param msg
     * @param clazz
     * @return
     */
    public static Object fromJson(String msg,Class clazz){
        try {
            return objectMapper.readValue(msg,clazz);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  将Json字符串转为对象
     * @param msg
     * @param typeReference
     * @return
     */
    public static Object fromJson(String msg, TypeReference typeReference){
        try{
            return objectMapper.readValue(msg,typeReference);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
