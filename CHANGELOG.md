# Changelog

## [1.0.1] - 2026-08-08

### Changed

- `ErrorCode` 现在会在解析到非 12 位、非 `DE` 前缀或不支持的版本时抛出
  `IllegalArgumentException`，不再静默替换为 `CODE_PROCESSING_ERROR`。
- `ErrorCode(String errorCode, String version)` 对非 `"0"` 版本显式失败，异常消息包含实际版本值。
- `ErrorCode.builder()` 现在会保留受支持的默认版本 `"0"`，不再因版本为空而输出保留错误码。
- 明确 `ErrorContext` 中首个错误为根错误、最后一个错误为当前错误，并增加对应回归测试。
- 升级 Guava、Lombok、JUnit 与 Maven 插件到当前稳定版本，继续保持 Java 17 字节码基线。
- 显式注册 Lombok 注解处理器，确保使用 JDK 23 及更高版本构建时仍可正确生成代码。
- 固定带 SHA-256 校验的 Maven 3.9.16 Wrapper，并增加 Java/Maven 版本和依赖收敛约束。
- 迁移至 Maven Central Portal，新增 CI、Dependabot、Snapshot 发布、签名正式版发布及公开解析校验。

### Compatibility

这是一次行为收紧：传入合法的 12 位 `DE`、版本 `0` 错误码的调用方不受影响；此前依赖静默降级的
调用方会在构造时收到显式异常，从而可修正错误码或版本配置。版本由 `1.0.0` 上调至 `1.0.1`。
