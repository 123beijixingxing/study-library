# 学习通 Apifox 导入说明

## 1. 可导入文件

- OpenAPI 草案：`docs/需求设计文档/学习通OpenAPI草案.yaml`
- Postman 集合草案：`docs/需求设计文档/学习通接口联调-PostmanCollection草案.json`
- Postman 扩展集合草案：`docs/需求设计文档/学习通接口联调-PostmanCollection扩展草案.json`
- Postman 环境草案：`docs/需求设计文档/学习通接口联调-Postman环境草案.json`

## 2. 推荐导入方式

### 方式一：导入 OpenAPI

适合需要快速生成接口目录、参数模型、响应模型、在线调试文档的场景。

步骤：
1. 在 Apifox 新建项目。
2. 选择“导入” -> “OpenAPI/Swagger”。
3. 选择 `docs/需求设计文档/学习通OpenAPI草案.yaml`。
4. 导入后检查：
   - 服务地址是否识别为 `/api/app/v1` 和 `/api/admin/v1`
   - Schema 是否正确生成
   - 路径参数、分页参数、状态字段是否与接口文档一致

### 方式二：导入 Postman 集合

适合需要直接复用联调请求示例和环境变量的场景。

步骤：
1. 在 Apifox 选择“导入” -> “Postman Collection”。
2. 优先选择 `docs/需求设计文档/学习通接口联调-PostmanCollection扩展草案.json`；若只需要基础集合，可选 `docs/需求设计文档/学习通接口联调-PostmanCollection草案.json`。
3. 导入环境变量时，可参考 `docs/需求设计文档/学习通接口联调-Postman环境草案.json` 手动建立环境。
4. 若只做前台联调，可先按集合目录导入 `App` 相关分组；若做后台联调，优先导入 `Admin` 相关分组和扩展集合。

## 3. 推荐环境变量

| 变量名 | 示例值 | 说明 |
| --- | --- | --- |
| `appBaseUrl` | `/api/app/v1` | 前台接口基础路径 |
| `adminBaseUrl` | `/api/admin/v1` | 后台接口基础路径 |
| `appToken` | `app_access_token_xxx` | 前台 Bearer Token |
| `adminToken` | `admin_access_token_xxx` | 后台 Bearer Token |

## 4. 导入后建议检查项

1. 核对接口路径是否与 `docs/需求设计文档/学习通接口清单.md` 一致。
2. 核对模型字段是否与 `docs/需求设计文档/学习通接口DTO字段字典.md` 一致。
3. 核对错误码与响应结构是否与 `docs/需求设计文档/学习通前后端状态码规范.md` 一致。
4. 核对状态前置条件是否可在 Apifox 文档备注中体现。

## 5. 后续建议

1. 若后续继续扩充 `学习通OpenAPI草案.yaml`，建议优先以 OpenAPI 为主源，再同步导出 Postman 集合。
2. 若要进入正式联调，可继续补充 Apifox 中的示例响应、错误响应和目录分组说明。
3. 若需要，我可以继续把 OpenAPI 草案扩到更接近完整项目接口清单的覆盖范围，并同步刷新 Postman 扩展集合。
