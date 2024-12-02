package io.github.archetom.common.error;

import lombok.Data;

import java.io.Serializable;

/**
 * 标准错误对象
 * <p>标准错误对象包含</p>
 * <ul>
 * <li>标准错误码</li>
 * <li>错误默认文案</li>
 * <li>错误产生位置</li>
 * </ul>
 * <p>标准错误对象是一次错误处理结果的描述</p>
 */
@Data
public class CommonError implements Serializable {
    private static final long serialVersionUID = -3515464594103532397L;

    /**
     * 错误编码
     */
    private io.github.archetom.common.error.ErrorCode errorCode;
    /**
     * 错误描述
     */
    private String errorMsg;
    /**
     * 错误发生系统
     */
    private String location;

    /**
     * 默认构造方法
     */
    public CommonError() {
    }

    /**
     * 全参数构造方法
     *
     * @param code     错误码
     * @param msg      错误描述
     * @param location 错误发生系统
     */
    public CommonError(ErrorCode code, String msg, String location) {
        this.errorCode = code;
        this.errorMsg = msg;
        this.location = location;
    }

    /**
     * 构造错误摘要信息
     *
     * @return 错误摘要
     */
    public String toDigest() {
        return this.errorCode + "@" + this.location;
    }

    /**
     * 重写 ToString
     */
    @Override
    public String toString() {
        return this.errorCode + "@" + this.location + "::" + this.errorMsg;
    }
}
