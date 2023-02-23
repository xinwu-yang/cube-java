package org.cube.commons.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口返回数据结构
 */
@Data
@NoArgsConstructor
@Schema(title = "接口返回数据结构")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求成功标志
     */
    @Schema(title = "请求成功标志")
    private boolean success = true;

    /**
     * 返回处理消息
     */
    @Schema(title = "返回处理消息")
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    @Schema(title = "返回代码")
    private Integer code = 200;

    /**
     * 返回数据对象
     */
    @Schema(title = "返回数据对象")
    private T result;

    /**
     * 请求时间戳
     */
    @Schema(title = "请求时间戳")
    private long timestamp = System.currentTimeMillis();

    /**
     * 成功
     *
     * @param data 返回数据
     */
    public Result<T> success(T data) {
        this.result = data;
        return this;
    }

    /**
     * 失败
     *
     * @param message 错误消息
     */
    public Result<T> fail(String message) {
        this.success = false;
        this.code = 500;
        this.message = message;
        return this;
    }

    /**
     * 成功
     *
     * @param <T> 泛型
     * @return 默认消息
     */
    public static <T> Result<T> ok() {
        return new Result<>();
    }

    /**
     * 成功并带有返回值
     *
     * @param <T> 泛型
     * @return 泛型返回值
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        return result.success(data);
    }

    /**
     * 成功并带有消息和返回值
     *
     * @param msg  消息
     * @param data 返回值
     * @param <T>  泛型
     * @return 消息和泛型返回值
     */
    public static <T> Result<T> ok(String msg, T data) {
        Result<T> result = new Result<>();
        result.setMessage(msg);
        result.setResult(data);
        return result;
    }

    /**
     * 默认code为500并带有错误消息
     *
     * @param msg 消息
     * @param <T> 泛型
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        return result.fail(msg);
    }

    /**
     * 指定错误码并带有错误消息
     *
     * @param code 错误码
     * @param msg  消息
     * @param <T>  泛型
     */
    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(msg);
        result.setSuccess(false);
        return result;
    }

    /**
     * 指定错误码并带有返回值和错误消息
     *
     * @param code       错误码
     * @param msg        消息
     * @param returnData 返回数据
     */
    public static Result<?> error(int code, String msg, Object returnData) {
        Result<Object> result = new Result<>();
        result.setCode(code);
        result.setMessage(msg);
        result.setSuccess(false);
        result.setResult(returnData);
        return result;
    }
}