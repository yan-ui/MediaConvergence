package cn.tklvyou.mediaconvergence.base;

/***
 * 基础数据结构
 */
public class BaseResult<T> {

    private int code;
    private Object msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code=" + code +
                ", msg=" + msg +
                ", data=" + data +
                '}';
    }
}