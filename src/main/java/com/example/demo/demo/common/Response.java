package com.example.demo.demo.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author xugm
 * @create 2022/1/11 9:29
 */
public class Response<T> implements Responsive, Serializable {
    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private static final long serialVersionUID = -5413501204183149353L;
    private String code;
    private String msg;
    private T data;

    private Response() {
    }

    private Response(T data) {
        this(data, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    private Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Response(T data, String code, String msg) {
        this(code, msg);
        this.data = data;
    }

    public static Response<Boolean> ok() {
        return new Response(true);
    }

    public static <E> Response<E> ok(E data) {
        return new Response(data);
    }

    public static <E> Response<E> ok(E data, String msg) {
        return new Response(data, ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <E> Response<E> failure() {
        return failure(ResponseCode.SYSTEM_ERROR.getMessage());
    }

    public static <E> Response<E> failure(String msg) {
        return failure(ResponseCode.SYSTEM_ERROR.getCode(), msg);
    }

    public static <E> Response<E> failure(String code, String msg) {
        return new Response(code, msg);
    }

    public static <E> Response<E> failure(ResponseCode responseCode) {
        return new Response(responseCode.getCode(), responseCode.getMessage());
    }

    public static <E> Response<E> failure(E data, String code, String msg) {
        return new Response(data, code, msg);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return Objects.equals(this.code, ResponseCode.SUCCESS.getCode());
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Response)) {
            return false;
        } else {
            Response<?> other = (Response)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label47;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label47;
                    }

                    return false;
                }

                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if (this$msg == null) {
                    if (other$msg != null) {
                        return false;
                    }
                } else if (!this$msg.equals(other$msg)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Response;
    }



    public String toString() {
        return "Response(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }
}
