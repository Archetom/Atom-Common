# Changelog

## [1.2.0] - 2026-10-29

### Changed

- 发布版本升级为 `1.2.0`，编译、测试、CI 和发布运行时从 Java 25 升级到 Java 27。
- 将最低 Maven 版本从 `3.9.16` 提升为 `3.10.0`，与已有的 Maven Wrapper 分发版本对齐；
  Maven `3.10.0` 和 Wrapper `3.3.4` 均为截至 2026-10-29 的最新稳定版。
- 升级 Maven Surefire Plugin `3.6.0` → `3.6.1`、Central Publishing Maven Plugin
  `0.11.0` → `0.12.0`、Versions Maven Plugin `2.22.0` → `2.23.0`。
  Central Publishing `0.12.0` 修复了 Maven 3.10.0 暂存额外元数据导致 Central 拒收的问题。
- CI、正式版和 Snapshot 发布工作流统一升级 `actions/setup-java` `6.0.1` → `6.1.0`，
  保持完整提交 SHA 固定和 Temurin 最新 Java 27 补丁版本解析。
- 核对并保留已是最新稳定版的 Lombok `1.18.48`（支持 JDK 27）、Guava `33.7.2-jre`、
  JUnit Jupiter `6.1.3`、其余构建插件及 `actions/checkout` `7.0.1`。

### Compatibility

`1.2.0` 生成 Java 27 字节码，消费方构建和运行时必须使用 Java 27 或更高版本；仍需兼容 Java 25
的项目应继续使用已发布的 `1.1.x`。公共 API、序列化字段和现有空值处理契约不变。

## [1.1.0] - 2026-08-08

### Changed

- 将编译、测试、CI 和发布运行时从 Java 17 对齐至 Atom Archetype 使用的 Java 25。
- 将 Lombok 固定为 `1.18.46`、JUnit Jupiter 对齐至 Spring Boot 4.1.0 管理的 `6.0.3`，并继续使用
  Guava `33.6.0-jre`。
- 发布坐标由 `io.github.archetom:atom-common:1.0.1` 更新为 `1.1.0`。
- 重写项目首页，聚焦使用要求、依赖坐标和核心 API，移除面向仓库维护者的发布配置细节。

### Compatibility

`1.1.0` 生成 Java 25 字节码，消费方构建和运行时必须使用 Java 25 或更高版本；仍需兼容 Java 17 的
项目应继续使用 `1.0.x`。

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
