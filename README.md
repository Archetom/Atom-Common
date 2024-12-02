# Atom Common

`Atom Common` 是一个通用的 Java 工具库，提供了常用的错误处理、结果封装、分页功能以及性能分析工具，用于 [Atom Archetype](https://github.com/Archetom/atom-archetype) 项目。

---

## 特性

- **错误处理**：提供标准化的错误码 (`ErrorCode`) 和错误上下文 (`ErrorContext`) 支持，便于统一管理和传递错误信息。
- **结果封装**：通过 `Result` 和 `Pager` 类支持标准化的服务结果返回和分页结果封装。
- **性能分析**：提供 `Profiler` 工具类，用于统计线程执行时间的分布和耗时。

---

## 项目结构

```plaintext
src/main/java/io/github/archetom/common
├── error                       # 错误处理模块
│   ├── CommonError.java        # 标准错误对象
│   ├── ErrorCode.java          # 标准错误码
│   ├── ErrorContext.java       # 错误上下文
│   ├── ReservedErrorCode.java  # 保留错误码定义
├── result                      # 结果封装模块
│   ├── BaseToString.java       # toString 包装工具
│   ├── Pager.java              # 分页结果封装
│   ├── Result.java             # RPC 服务结果封装
├── utils                       # 工具类模块
│   ├── Profiler.java           # 性能分析工具
```

---

## 快速开始

### 1. 添加依赖

在你的 `pom.xml` 中引入 `Atom Common` 库：

```xml
<dependency>
    <groupId>io.github.archetom</groupId>
    <artifactId>atom-common</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 使用示例

#### 1. 错误码定义与处理

```java
import io.github.archetom.common.error.CommonError;
import io.github.archetom.common.error.ErrorCode;
import io.github.archetom.common.error.ErrorContext;

public class Example {
    public static void main(String[] args) {
        // 定义错误码
        ErrorCode errorCode = new ErrorCode("0", "1", "0", "0001", "001");

        // 创建标准错误对象
        CommonError error = new CommonError(errorCode, "系统异常", "系统模块");

        // 添加到错误上下文
        ErrorContext errorContext = new ErrorContext();
        errorContext.addError(error);

        // 打印错误上下文摘要
        System.out.println(errorContext.toDigest());
    }
}
```

#### 2. 分页结果封装

```java
import io.github.archetom.common.result.Pager;

import java.util.Arrays;

public class Example {
    public static void main(String[] args) {
        Pager<String> pager = new Pager<>(
            Arrays.asList("记录1", "记录2", "记录3"), // 数据列表
            10,  // 每页大小
            1,   // 当前页码
            30L, // 总条目数
            null // 额外元数据
        );

        System.out.println(pager);
    }
}
```

#### 3. 性能分析工具

```java
import io.github.archetom.common.utils.Profiler;

public class Example {
    public static void main(String[] args) throws InterruptedException {
        Profiler.start("任务开始");
        Thread.sleep(100); // 模拟任务1耗时

        Profiler.enter("子任务1");
        Thread.sleep(200); // 模拟任务2耗时
        Profiler.release();

        Profiler.enter("子任务2");
        Thread.sleep(300); // 模拟任务3耗时
        Profiler.release();

        Profiler.release(); // 结束主任务
        System.out.println(Profiler.dump());
    }
}
```

## 许可证

本项目采用 [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0) 开源。
