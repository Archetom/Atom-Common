package io.github.archetom.common.error;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 标准错误码格式如下：
 * | 位置  | 1   | 2   | 3   | 4   | 5   | 6   | 7   | 8   | 9   | 10  | 11  | 12  |
 * |-------|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|
 * | 示例  | D   | E   | 0   | 1   | 0   | 1   | 0   | 1   | 1   | 0   | 2   | 7   |
 * 解释：
 * - **位置 1-2：错误码前缀**，固定为 "DE"。
 * - **位置 3：版本号**，用于支持未来的错误码升级。
 * - **位置 4：错误级别**，表示错误的严重程度（例如致命、警告、信息）。
 * - **位置 5：错误类型**，表示错误所属的类别。
 * - **位置 6-9：场景码**，表示错误的发生场景或具体事件。
 * - **位置 10-12：具体错误码**，表示详细的错误原因。
 * <p>
 * 当前实现仅支持版本 {@value #DEFAULT_VERSION}。解析的错误码或构造方法指定了其他版本时，
 * 会抛出 {@link IllegalArgumentException}，不会替换为保留错误码。
 */
@Data
@Builder
@AllArgsConstructor
public class ErrorCode implements Serializable {
    private static final long serialVersionUID = -7187816449021846174L;

    /**
     * 统一错误规范默认版本
     */
    private static final String DEFAULT_VERSION = "0";

    /**
     * 固定标识【第1-2位】
     */
    private static final String PREFIX = "DE";

    /**
     * 错误前缀 【第1-2位】
     */
    private String errorPrefix;

    /**
     * 规范版本【第3位】
     */
    private String version = DEFAULT_VERSION;

    /**
     * 错误级别【第4位，见 <code>ErrorLevelConst</code>定义
     */
    private String errorLevel;

    /**
     * 错误类型【第5位】，见<code>ErrorType</code>定义
     */
    private String errorType;

    /**
     * 错误场景【第6-9位】即事件编码 EventCode 每个应用需要单独申请
     */
    private String errorScene;

    /**
     * 具体错误码【第10-12位】
     */
    private String errorSpecific;

    /**
     * 默认构造方法
     */
    public ErrorCode() {
    }

    /**
     * 构造方法
     *
     * @param errorCode 错误码
     * @throws IllegalArgumentException 如果错误码不是 12 位、前缀不是 {@code DE}，或包含不支持的版本
     */
    public ErrorCode(String errorCode) {
        buildXDErrorCode(errorCode);
    }

    /**
     * 构造方法
     *
     * @param errorCode 错误码
     * @param version   版本
     * @throws IllegalArgumentException 如果 {@code version} 不受支持，或错误码格式无效
     */
    public ErrorCode(String errorCode, String version) {
        validateSupportedVersion(version);
        buildXDErrorCode(errorCode);
    }

    /**
     * 构造方法
     *
     * @param errorLevel    错误级别
     * @param errorType     错误类型
     * @param errorScene    错误场景
     * @param errorSpecific 具体错误码
     */
    public ErrorCode(String errorLevel, String errorType, String errorScene, String errorSpecific) {
        this.errorSpecific = errorSpecific;
        this.errorLevel = errorLevel;
        this.errorType = errorType;
        this.errorScene = errorScene;
    }

    /**
     * 构造方法
     *
     * @param version       错误版本
     * @param errorLevel    错误级别
     * @param errorType     错误类型
     * @param errorScene    错误场景
     * @param errorSpecific 具体错误码
     * @throws IllegalArgumentException 如果 {@code version} 不受支持
     */
    public ErrorCode(String version, String errorLevel, String errorType, String errorScene, String errorSpecific) {
        validateSupportedVersion(version);
        this.errorSpecific = errorSpecific;
        this.version = version;
        this.errorLevel = errorLevel;
        this.errorType = errorType;
        this.errorScene = errorScene;
    }

    /**
     * 构造错误码。
     * <p>
     * 此方法采用严格校验：不合法的输入直接抛出异常，而不再静默替换为
     * {@link ReservedErrorCode#CODE_PROCESSING_ERROR}。错误码属于调用方的业务契约，替换后会掩盖
     * 原始输入并将业务错误错误地报告为内部处理错误。
     *
     * @param errorCode 错误码
     * @throws IllegalArgumentException 如果错误码格式或版本不受支持
     */
    private void buildXDErrorCode(String errorCode) {
        try {
            checkString(errorCode, 12);
            splitErrorCode(errorCode);
            validateSupportedVersion(this.version);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid error code '" + errorCode + "': " + e.getMessage(), e);
        }
    }

    /**
     * 校验当前实现能够解析的错误码版本。
     *
     * @param version 错误码版本
     * @throws IllegalArgumentException 如果版本不是 {@value #DEFAULT_VERSION}
     */
    private void validateSupportedVersion(String version) {
        if (!Objects.equal(version, DEFAULT_VERSION)) {
            throw new IllegalArgumentException("Unsupported error code version: " + version);
        }
    }

    /**
     * 将当前错误码对象转换为标准的错误码字符串格式。
     * <p>
     * 如果对象中的字段不符合错误码的标准格式，会返回保留错误码 {@link ReservedErrorCode#CODE_PROCESSING_ERROR}。
     * </p>
     *
     * @return 返回标准格式的错误码字符串，例如 "DE0010010027"。
     */
    @Override
    public String toString() {
        try {
            checkString(this.version, 1);
            checkString(this.errorLevel, 1);
            checkString(this.errorScene, getErrorSceneLenByVersion(this.version));
            checkString(this.errorSpecific, 3);
            checkString(this.errorType, 1);
        } catch (IllegalArgumentException e) {
            return ReservedErrorCode.CODE_PROCESSING_ERROR;
        }

        return getPrefixByVersion(this.version) +
                this.version +
                this.errorLevel +
                this.errorType +
                this.errorScene +
                this.errorSpecific;
    }

    /**
     * 通过版本 来 获取错误前缀
     */
    private String getPrefixByVersion(String version) {
        if (Objects.equal(DEFAULT_VERSION, version)) {
            return PREFIX;
        }
        return this.errorPrefix;
    }

    /**
     * 通过版本 来 获取场景码
     */
    private int getErrorSceneLenByVersion(String version) {
        if (Strings.isNullOrEmpty(version.trim())) {
            throw new IllegalArgumentException();
        }
        if (Objects.equal(DEFAULT_VERSION, version)) {
            return 4;
        }
        return 8;
    }

    /**
     * 解析错误码字符串并将其分解为对应的字段。
     * <p>
     * 错误码必须以 "DE" 开头，长度为 12 位。
     * 如果错误码格式不符合要求，会抛出 {@link IllegalArgumentException} 异常。
     * </p>
     *
     * @param errorCode 待解析的错误码字符串。
     * @throws IllegalArgumentException 如果错误码格式不符合要求。
     */
    private void splitErrorCode(String errorCode) {
        if (!errorCode.startsWith(PREFIX)) {
            throw new IllegalArgumentException();
        }
        List<String> parts = Splitter.fixedLength(1).splitToList(errorCode);
        this.errorPrefix = parts.get(0) + parts.get(1);
        this.version = parts.get(2);
        this.errorLevel = parts.get(3);
        this.errorType = parts.get(4);
        this.errorScene = String.join("", parts.subList(5, 9));
        this.errorSpecific = String.join("", parts.subList(9, 12));
    }

    /**
     * 错误码长度检查 默认12位
     */
    private void checkString(String str, int length) {
        Preconditions.checkArgument(str != null && str.length() == length, "String must be %s characters long", length);
    }
}
