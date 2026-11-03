# 人生数据库（life-db）

一个记录每天睡眠、情绪、开销和"做了什么"的网页应用，每周自动生成一页复盘，外加一张"全面发展矩阵"，把人生分成六个方面逐项评估。电脑和手机浏览器都能用。

起因：想按"先记录 2～4 个月数据，再让 AI 分析"的方法管理自己的生活，现成工具要么数据不在自己手里，要么填起来太麻烦，于是自己做一个。

## 技术栈
- Java 21 + Spring Boot 4.1
- Spring Data JPA
- 数据库：开发用 H2（免安装），正式用 MySQL
- 前端：单个 HTML 页面 + 原生 JavaScript，适配手机

## 功能路线

| 版本 | 内容 | 状态 |
|---|---|---|
| v0.1 | 每日记录的增删改查；参数校验和统一错误处理；适配手机的页面：连续记录天数、情绪日历（热力图）、睡眠时间带、时间线 | ✅ 完成 |
| v0.1.1 | 全面发展矩阵：身体、精神、学习、事业、社交、财务六个方面，每项标现状和优先级；雷达图、现状分布、"该先改的"清单、一键模板 | ✅ 完成 |
| v0.1.2 | 每周复盘页：一句话总结、和上周对比、七个夜晚、情绪走势、开销、自动发现、一键复制给 AI | ✅ 完成 |
| v0.2 | 登录（Spring Security），数据只有自己能看 | ⬜ |
| v0.3 | 统计：每周平均睡眠、情绪趋势、开销合计；Redis 缓存统计结果 | ⬜ |
| v0.4 | 接入大模型：一句话自动整理成记录；每周自动生成分析报告 | ⬜ |
| v0.5 | 切换 MySQL，部署到云服务器 | ⬜ |

## 本地运行

需要先装 **JDK 21**。

```bash
# 在项目根目录
mvn spring-boot:run
```

没装 Maven 的话，用 IntelliJ IDEA 打开项目，直接运行 `LifeDbApplication` 也可以。

启动后：
- 日记页面：<http://localhost:8080>
- 复盘页面：<http://localhost:8080/review.html>
- 矩阵页面：<http://localhost:8080/matrix.html>
- 查看数据库：<http://localhost:8080/h2-console>，JDBC URL 填 `jdbc:h2:file:./data/lifedb`，用户名 `sa`，密码留空

数据存在项目下的 `data/` 目录，已在 `.gitignore` 里排除，不会提交到仓库。

### 手机访问（同一个 WiFi）
1. 电脑上查局域网 IP：Windows 用 `ipconfig`，Mac 用 `ipconfig getifaddr en0`
2. 手机浏览器打开 `http://电脑IP:8080`
3. 打不开的话，检查电脑防火墙是否放行了 8080 端口

## 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/records` | 全部记录，按日期倒序 |
| GET | `/api/records/{date}` | 某一天的记录，日期格式 `2026-11-01` |
| PUT | `/api/records/{date}` | 保存某一天：有就更新，没有就新建 |
| DELETE | `/api/records/{date}` | 删除某一天 |
| GET | `/api/items` | 矩阵的全部项 |
| POST | `/api/items` | 新建一项 |
| PUT | `/api/items/{id}` | 修改一项 |
| DELETE | `/api/items/{id}` | 删除一项 |

## 运行测试
```bash
mvn test
```

## 学习笔记
每个版本用到的原理和可能被问到的面试题，见 [docs/学习笔记.md](docs/学习笔记.md)。
