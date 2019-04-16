package cn.sell.product.utils;

import cn.sell.product.VO.ResultVO;

public class ResultVOUtil {
    public static ResultVO success(Object o){

        ResultVO resultVO=new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(o);
        return resultVO;
    }
}
