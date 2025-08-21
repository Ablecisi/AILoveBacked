# AILoveBacked

基于 Spring Boot 3 的 AI 伴侣后台服务，提供角色管理、实时聊天、文章与评论等功能。项目使用 MyBatis 操作 MySQL，结合 Redis 缓存，并集成 JWT 认证与阿里云 OSS 文件上传能力。

## 功能特性
- **角色系统**：获取热门角色、角色详情等接口
- **用户模块**：支持用户登录、关注/取消关注作者
- **会话与消息**：管理会话、发送/拉取消息，并通过 WebSocket 实时推送
- **文章与评论**：文章发布与评论交互
- **文件上传**：上传文件至阿里云 OSS

## 技术栈
- Java 21 / Spring Boot 3
- WebSocket, MyBatis, MySQL, Redis
- Lombok, JWT, PageHelper, Fastjson2, OSS SDK
- 本地 BERT 服务与通用 LLM API

## 目录结构
```text
AILoveBacked
├─ pom.xml
├─ src
│  ├─ main
│  │  ├─ java/com/ablecisi/ailovebacked
│  │  │  ├─ controller        # REST 控制器
│  │  │  ├─ service & impl    # 业务逻辑
│  │  │  ├─ mapper            # MyBatis 映射接口
│  │  │  ├─ websocket         # WebSocket 推送
│  │  │  └─ ...
│  │  └─ resources
│  │     ├─ application.yml   # 配置文件
│  │     ├─ mapper/*.xml      # MyBatis SQL
│  │     └─ db/init.sql       # 数据库初始化脚本
└─ README.md
```

## 快速开始
### 环境要求
- JDK 21+
- Maven 3.x 或使用项目自带的 `mvnw`
- MySQL 8.x
- Redis 6.x
- 已申请的 LLM/API 与阿里云 OSS 账号（可选）

### 运行步骤
```bash
# 克隆项目
git clone <repo-url>
cd AILoveBacked

# 配置环境变量或修改 application.yml
# (见 src/main/resources/application.yml)

# 编译
./mvnw clean package

# 启动
./mvnw spring-boot:run
# 或运行打包后的 jar
# java -jar target/AILoveBacked-0.0.1-SNAPSHOT.jar
```

数据库初始化脚本位于 `src/main/resources/db/init.sql`，可导入 MySQL 创建表结构。

## 许可证
项目采用 [MIT License](LICENSE) 授权，欢迎提交 Issue 或 PR 共同完善项目。

