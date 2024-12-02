package io.github.archetom.common.error;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 错误上下文对象
 * <p>
 * 错误上下文对象包含一个错误堆栈以及第三方错误信息，用于记录错误发生的历史路径。
 * <p>
 * 使用场景：
 * <ul>
 *   <li>当需要捕获并传递多个错误时，可以利用此类构建一个错误堆栈。</li>
 *   <li>在复杂的调用链中传递上下文信息。</li>
 * </ul>
 */
@Data
public class ErrorContext implements Serializable {
    private static final long serialVersionUID = 8859774133335862382L;

    /**
     * 错误堆栈集合
     */
    private List<CommonError> errorStack = new ArrayList<>();

    /**
     * 第三方错误原始信息
     */
    private String thirdPartyError;

    /**
     * 默认分割符
     */
    private static final String SPLIT = "|";

    /**
     * 获取当前错误对象
     *
     * @return CommonError 标准错误对象
     */
    public CommonError fetchCurrentError() {
        if ((this.errorStack != null) && (!this.errorStack.isEmpty())) {
            return this.errorStack.get(this.errorStack.size() - 1);
        }
        return null;
    }

    /**
     * 获取当前错误编码
     *
     * @return 当前错误编码
     */
    public String fetchCurrentErrorCode() {
        if ((this.errorStack != null) && (!this.errorStack.isEmpty())) {
            return this.errorStack.get(this.errorStack.size() - 1).getErrorCode().toString();
        }
        return null;
    }

    /**
     * 获取原始错误对象
     *
     * @return 原始错误对象
     */
    public CommonError fetchRootError() {
        if ((this.errorStack != null) && (!this.errorStack.isEmpty())) {
            return this.errorStack.get(0);
        }
        return null;
    }

    /**
     * 向堆栈中添加错误对象
     *
     * @param error 添加的错误信息
     */
    public void addError(CommonError error) {
        if (this.errorStack == null) {
            this.errorStack = new ArrayList<>();
        }
        this.errorStack.add(error);
    }

    /**
     * 错误数组 摘要日志构建
     *
     * @return 日志摘要
     */
    public String toDigest() {
        return Joiner.on("|").skipNulls().join(
                Lists.reverse(this.errorStack).stream().map(CommonError::toDigest).toArray()
        );
    }

    /**
     * 摘要日志
     */
    private String digest(CommonError commonError) {
        if (null == commonError) {
            return null;
        }
        return commonError.toDigest();
    }

    /**
     * toString 重写
     */
    @Override
    public String toString() {
        return Joiner.on("|")
                .join(Lists.reverse(this.errorStack));
    }
}
