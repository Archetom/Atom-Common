# Changelog

## [1.0.1] - Unreleased

### Changed

- `ErrorCode` 现在会在解析到非 12 位、非 `DE` 前缀或不支持的版本时抛出
  `IllegalArgumentException`，不再静默替换为 `CODE_PROCESSING_ERROR`。
- `ErrorCode(String errorCode, String version)` 对非 `"0"` 版本显式失败，异常消息包含实际版本值。

### Compatibility

这是一次行为收紧：传入合法的 12 位 `DE`、版本 `0` 错误码的调用方不受影响；此前依赖静默降级的
调用方会在构造时收到显式异常，从而可修正错误码或版本配置。版本由 `1.0.0` 上调至 `1.0.1`，本次
仅供本地安装验证，未发布。
