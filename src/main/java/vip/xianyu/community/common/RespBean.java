package vip.xianyu.community.common;

/**
 * @Author:asus
 * @Date: 2021/3/2 23:34
 * @Description:返回信息实体类
 */

public class RespBean {
    private int code;
    private String msg;
    private Object data;

    public RespBean() {
    }

    public RespBean(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static RespBean success(String msg){
        return new RespBean(200,msg,null);
    }
    public static RespBean success(String msg,Object data){
        return new RespBean(200,msg,data);
    }
    public static RespBean error(String msg){
        return new RespBean(500,msg,null);
    }
    public static RespBean error(String msg,Object data){
        return new RespBean(500,msg,data);
    }

    @Override
    public String toString() {
        return "RespBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
