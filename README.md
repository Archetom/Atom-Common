# Atom Common

Atom Common 是 Atom 项目的共享基础类型库，提供错误码、错误上下文、统一结果、分页结果和轻量级耗时分析。
它由 [Atom Archetype](https://github.com/Archetom/atom-archetype) 生成的项目使用，也可以独立引入普通
Java 应用；库本身不依赖 Spring。

## 环境要求

- Atom Common 1.1.x：Java 25 或更高版本
- Atom Common 1.0.x：Java 17 或更高版本

从源码构建时无需预装指定版本的 Maven，仓库已包含 Maven 3.9.16 Wrapper。

## 添加依赖

```xml
<dependency>
    <groupId>io.github.archetom</groupId>
    <artifactId>atom-common</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 主要类型

| 类型 | 用途 |
| --- | --- |
| `ErrorCode` | 组装和解析统一的 12 位错误码 |
| `CommonError` | 描述错误码、错误信息和发生位置 |
| `ErrorContext` | 按发生顺序保存一条错误传播链 |
| `Result<T>` | 表示成功状态、结果数据和错误上下文 |
| `Pager<T>` | 表示分页数据，并支持保持分页信息的类型转换 |
| `Profiler` | 记录当前线程内可嵌套的执行耗时 |

### 错误码与错误上下文

当前错误码格式为 `DE + 版本 + 级别 + 类型 + 场景码 + 具体码`。版本固定为 `0`，各字段长度分别为
`2 + 1 + 1 + 1 + 4 + 3`。解析非法长度、非法前缀或不支持的版本时会抛出
`IllegalArgumentException`。

```java
import io.github.archetom.common.error.CommonError;
import io.github.archetom.common.error.ErrorCode;
import io.github.archetom.common.error.ErrorContext;

ErrorCode code = new ErrorCode("1", "0", "0001", "001");
CommonError error = new CommonError(code, "客户不存在", "customer-service");

ErrorContext context = new ErrorContext();
context.addError(error);

System.out.println(code);                    // DE0100001001
System.out.println(context.fetchRootError());
System.out.println(context.fetchCurrentError());
```

`fetchRootError()` 返回最早加入的错误，`fetchCurrentError()` 返回最近加入的错误。

### 分页结果转换

```java
import io.github.archetom.common.result.Pager;

import java.util.List;
import java.util.Map;

Pager<String> page = new Pager<>(
        List.of("10", "20"),
        20,
        1,
        Pager.NO_TOTAL_NUM,
        Map.of("source", "search")
);

Pager<Integer> converted = page.map(Integer::valueOf);
```

`map` 会保留页码、每页数量、总数和元数据。无法或无需计算总数时，可使用 `Pager.NO_TOTAL_NUM`。

### 耗时分析

```java
import io.github.archetom.common.utils.Profiler;

Profiler.start("request");
Profiler.enter("load-customer");
// 执行业务逻辑
Profiler.release();
Profiler.release();

System.out.println(Profiler.dump());
Profiler.reset();
```

`Profiler` 使用线程本地状态，使用结束后应调用 `reset()` 清理。

## 从源码构建

```bash
sh ./mvnw clean verify -Dgpg.skip=true
```

该命令会运行测试，并生成主 JAR、源码包和 Javadoc 包。版本变更记录见 [CHANGELOG.md](CHANGELOG.md)。

## 许可证

本项目采用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 许可证。
