package cn.sell.user.utils;


import cn.sell.user.VO.ResultVO;
import cn.sell.user.enums.LoginErrorStatusEnum;

public class ResultVOUtil {
    public static ResultVO success(){

        ResultVO resultVO=new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }
    public static ResultVO error(LoginErrorStatusEnum loginErrorStatusEnum){

        ResultVO resultVO=new ResultVO();
        resultVO.setCode(loginErrorStatusEnum.getCode());
        resultVO.setMsg(loginErrorStatusEnum.getMsg());
        return resultVO;
    }
}
