# 有限空间作业服务

Java 25 / Spring Boot。运行 `mvn test`、`mvn package`。

## 配置

- `DB_URL`、`DB_USER`、`DB_PASSWORD`：数据库连接。
- `APP_USERS_FILE`：服务器上的私有账号文件，不能放进 Git。
- `APP_ORIGINS`：允许的 PC 前端来源，逗号分隔。
- `PORT`：默认 8092。

使用 `python3 scripts/create_account.py alice --role staff` 生成账号条目，保存到权限为 600 的账号文件。审批账号使用 `reviewer`；管理账号使用 `admin`。迁移旧数据时，使用 `--id` 指定旧记录所属 OpenID。密码采用 PBKDF2-HMAC-SHA256 加盐存储。

客户端通过 `POST /auth/login` 提交 username/password，后续请求使用返回的 Bearer token。会话有效期 8 小时，重启失效；生产环境启用 HTTPS。未配置账号时拒绝登录。旧 Wx/login 不再提供身份凭据。

上线前备份数据库、核对 UUID 重复记录后执行 `migrations/001_unique_records.sql`。审批仅允许待审批状态转换一次；签字、审批角色和非本人审批由服务端检查。现场记录提交使用持久化请求 ID，在事务中锁定关联申请，重复的相同提交返回原成功结果，内容冲突返回 409。

此前源码中的数据库凭据与小程序密钥需要在相应服务中轮换，删除源码中的值不会使旧凭据失效。
