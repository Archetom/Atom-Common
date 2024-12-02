package io.github.archetom.common.result;

import io.github.archetom.common.error.ErrorContext;
import lombok.Data;

import java.io.Serializable;

/**
 * RPC服务接口结果返回类
 *
 * @param <T> 返回类
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -2276974530199598142L;
    /**
     * 本次服务调用是否成功
     */
    private boolean success;

    /**
     * 错误上下文
     */
    private ErrorContext errorContext;

    /**
     * 结果对象
     */
    private T data;
}
