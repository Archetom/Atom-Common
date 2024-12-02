package io.github.archetom.common.error;

/**
 * 保留错误码，提供全局的定义
 */
public class ReservedErrorCode {

    /**
     * 未知异常
     */
    public final static String UNKNOWN_ERROR = "DE0599999999";

    /**
     * 未知系统异常
     */
    public final static String UNKNOWN_SYSTEM_ERROR = "DE0509999999";

    /**
     * 未知业务异常
     */
    public final static String UNKNOWN_BIZ_ERROR = "DE0519999999";

    /**
     * 未知第三方异常
     */
    public final static String UNKNOWN_THIRD_PARTY_ERROR = "DE0529999999";

    /**
     * 错误码工具内部异常
     */
    public final static String CODE_PROCESSING_ERROR = "DE0509998998";
}
