# study-library-admin

基于 `docs/demo/financial-expense-system` 的分层方式生成的学习通后台项目目录骨架。

目录设计原则：

- 延续 demo 项目的 `src/api`、`src/pages`、`src/router`、`src/store`、`src/utils` 等主干结构
- 结合当前学习通后台需求，提前拆出后台核心业务模块目录
- 已先落地基础启动文件、登录页、后台布局、路由、Vuex、Axios/API 初始化文件，便于后续直接扩展

当前已生成的基础能力：

- `package.json`、`vite.config.ts`、`tsconfig*.json`、`index.html`
- `src/main.ts`、`src/App.vue`、全局样式与插件注册
- `src/router`：登录页 + 后台布局 + 仪表盘/核心菜单路由
- `src/store`：登录态、侧边栏、访问标签页状态管理
- `src/api`：已接入用户、课程、园地、会员、消息、客服、系统、题库、试卷、运营扩展等模块 API
- `src/api/adapters`：已接入字段适配层，分离页面展示字段与正式接口字段
- 当前登录态已支持 `refreshToken` 持久化，并在接口 `401` 时自动调用 `/api/admin/v1/auth/refresh` 续签
- mock 层生成的时间字符串已统一为 `yyyy-MM-dd HH:mm:ss`，便于和正式后端返回保持一致
- 当前 `yarn build` 仍会出现 `@vueuse/core` 的 PURE 注释提示，该提示来自上游依赖打包注释位置，不影响构建产物可用性
- `src/languageApi`：已接入 `vue-i18n`，支持中英文基础切换
- `src/assets/images/svg`：已接入菜单 SVG 图标资源
- `src/constants/enums.ts`：统一状态枚举与下拉选项
- `src/utils/table.ts`：统一分页与表格工具函数
- 高流量列表页已开始统一接入分页兼容层，当前已覆盖用户、课程、会员、学习记录、评价、日志、消息、客服、题库、试卷、举报、搜索运营、推荐位、消息模板、敏感词、首页与园地等主要列表页
- 课程管理、客服管理、题库管理、练习试卷管理、会员套餐配置、消息管理、首页管理、园地管理、搜索运营、推荐位配置、消息模板、敏感词管理已接入删除动作，对应后端采用逻辑删除语义
- `src/pages/login/login.vue`：后台登录页
- `src/pages/dashboard/dashboard.vue`：后台首页
- 核心业务管理页：用户、首页、课程、园地、会员、消息、客服、系统、题库、统计、日志
- 扩展运营页：举报审核、会员套餐、成长规则、练习试卷、搜索运营、推荐位配置、消息模板、敏感词管理
- 二级业务页：用户详情、用户权限配置、课程详情管理、课程评价管理、学习记录管理、火热内容审核、分享内容审核、题目管理、题目详情配置、版本审计总台、试卷明细与分析
- 题库增强能力：题目模板、模板筛选、版本组筛选、版本说明筛选、版本记录、版本时间线、来源链路、来源图谱、来源路径流、来源模式筛选、版本范围筛选、时间线顺序筛选、选项批量生成、答案校验、解析预览、版本谱系、快速版本切换、版本差异对比、版本回滚、差异导出、版本链导出、导出记录管理、导出记录预览、导出记录重命名与删除、版本审计总台、操作日志详情、导出关键字筛选、导出分组统计、导出记录高级过滤、导出摘要、操作动作统计卡、详情密度切换
- 试卷分析增强能力：成绩分布、趋势对比、章节筛选、章节热度、章节趋势面板、错题 Top 榜、错题热度曲线、同课程试卷对比、课程内试卷排行、提交高峰时段、高峰对比图、多试卷趋势面板、筛选状态持久化、筛选方案保存、方案重命名、方案分组筛选、方案收藏、方案标签编辑、方案排序、筛选摘要展示、分析快照导出、导出记录搜索、导出记录排序、导出记录分组、导出记录重点标记、导出记录标签编辑、导出记录复用、快照回放、导出记录预览
- 公共后台能力：面包屑、标签页导航、语言切换、统一状态枚举、统一表格分页工具
- `src/mock`：本地 mock 数据、接口回包和基于 localStorage 的持久化演示数据层
- mock 支持基础增删改后的本地保留，可用于演示用户、课程、消息、系统配置等后台操作效果
- `.env.development` / `.env.production`：基础环境变量与 mock 开关
- `.env.example`：环境变量模板，可快速复制生成本地配置
- `.env.integration.example`：真实后端联调示例配置（推荐通过 Vite 代理转发到后端，避免浏览器跨域）
- 后端对接清单：`docs/需求设计文档/学习通后台前后端对接清单.md`
- 真实接口切换说明：`docs/需求设计文档/学习通后台真实接口切换与联调步骤.md`
- 模块级联调任务清单：`docs/需求设计文档/学习通后台模块级联调任务拆解清单.md`
- 联调执行看板模板：`docs/需求设计文档/学习通后台联调执行看板模板.md`
- 联调问题记录模板：`docs/需求设计文档/学习通后台联调问题记录模板.md`
- 联调验收检查表：`docs/需求设计文档/学习通后台联调验收检查表.md`
- 交付与验收清单：`docs/需求设计文档/学习通后台交付与验收清单.md`
- 子项目开发规范：`study-library-admin/AGENTS.md`

启动建议：

```bash
cd study-library-admin
yarn
yarn dev
```

默认本地 mock 登录账号可直接使用：`admin / 123456`

真实后端联调建议：

- 复制 `.env.integration.example` 为 `.env.development.local`
- 保持 `VITE_API_BASE_URL=/api/admin/v1`、`VITE_UPLOAD_BASE_URL=/api/file/v1`
- 设置 `VITE_USE_MOCK=false`
- 用 `VITE_DEV_PROXY_TARGET=http://127.0.0.1:28080` 指向本地后端
- 这样前端既可通过 `http://localhost:10001/` 访问，也可通过局域网地址访问，不会因为浏览器直连后端产生 CORS 问题

如需重置本地 mock 演示数据，可清除浏览器 `localStorage` 中的 `study-library-admin-mock-db`。

当前验证结果：

- `yarn` 已完成安装
- `yarn test:run` 已通过
- `yarn type-check` 已通过
- `yarn build` 已通过
- `yarn dev` 已可启动，默认地址为 `http://localhost:10001/`
- 当前构建仍有一个非阻塞上游警告：`@vueuse/core` 的 PURE 注释清理提示；Sass legacy JS API 和主包 chunk warning 已完成处理

当前结论：

- 当前后台功能建设阶段已完成，可作为可运行、可演示、可扩展、可联调的后台工程交付。
- 当前已具备基础前端单元测试能力，已覆盖分页工具和接口适配层代表性逻辑

兼容性说明：

- 为适配当前 Node 18 环境，已将 Vite 调整为 `5.x`
- 已补充 `fast-glob` 依赖以支持 `vite-plugin-svg-icons`

建议目录：

```text
study-library-admin/
  public/
    static/
      images/
      icons/
      languagePack/
  src/
    api/
      modules/
      utils/
    assets/
      images/
        png/
        svg/
      style/
    components/
      business/
      common/
      svgIcon/
    hooks/
    languageApi/
      locales/
    layout/
    pages/
      login/
      dashboard/
      userManage/
      userDetail/
      userPermission/
      homeManage/
      courseManage/
      courseDetailManage/
      courseEvaluationManage/
      gardenManage/
      hotContentAudit/
      shareContentAudit/
      reportAudit/
      memberManage/
      memberPackageManage/
      growthRuleManage/
      studyRecordManage/
      messageManage/
      messageTemplateManage/
      serviceManage/
      systemManage/
      statistics/
      logs/
      questionBankManage/
      practicePaperManage/
      searchOperationManage/
      recommendSlotManage/
      sensitiveWordManage/
    plugins/
      directives/
      elementPlus/
      globalProperties/
      pinia/
      svg/
    router/
      utils/
    store/
      modules/
    types/
    utils/
```

建议优先落地顺序：

1. `login`、`dashboard`、`layout`
2. `userManage`、`homeManage`、`courseManage`、`gardenManage`
3. `memberManage`、`messageManage`、`serviceManage`、`systemManage`
4. `statistics`、`logs`、题库与运营扩展模块
