# AGENTS.md

## Scope
- This file applies to `study-library-admin` only.
- Treat this directory as an independent Vue 3 + TypeScript + Vite admin project.
- Prefer edits inside `src`; update `public` only for static assets or language-pack files.
- Do not edit `node_modules`, `dist`, or generated cache files.

## Project Summary
- App type: learning-platform admin console
- Stack: Vue 3, TypeScript, Vite, Vue Router, Vuex, Element Plus
- Optional local demo mode: mock API + localStorage persistence
- i18n: `vue-i18n` with Chinese and English locales
- SVG icons: `vite-plugin-svg-icons`

## Important Paths
- Entry: `src/main.ts`
- App shell: `src/App.vue`
- Layout: `src/layout/AdminLayout.vue`
- Router: `src/router/index.ts`
- Menu config: `src/router/utils/routerConfig.ts`
- Store: `src/store/index.ts`
- API modules: `src/api/modules/*.ts`
- API adapters: `src/api/adapters/admin.ts`
- Mock DB: `src/mock/db.ts`
- Mock modules: `src/mock/modules/*.ts`
- Shared enums: `src/constants/enums.ts`
- Table helpers: `src/utils/table.ts`
- Locales: `src/languageApi/locales/*.json`
- SVG icons: `src/assets/images/svg/*.svg`

## Commands
### Install
```bash
yarn
```

### Dev
```bash
yarn dev
```
- Default port: `10001`

### Build
```bash
yarn build
```
- Current expected non-blocking warnings: Sass legacy JS API deprecation warnings and Rollup PURE-annotation cleanup warnings from `@vueuse/core`.

### Type Check
```bash
yarn type-check
```

### Preview
```bash
yarn preview
```

### Format
```bash
yarn format
```

### Tests
```bash
yarn test
yarn test:run
```
- Single test file:
```bash
yarn test src/utils/table.test.ts
```
- Current first-party coverage is lightweight and focuses on utility and adapter regressions.

## Environment
- Development env file: `.env.development`
- Production env file: `.env.production`
- Template: `.env.example`
- Real-backend template: `.env.integration.example`
- `VITE_USE_MOCK=true` enables local mock mode.
- For real backend integration, switch `VITE_USE_MOCK=false` and point `VITE_API_BASE_URL` to the real admin API.

## Mock / Demo Data Rules
- Mock storage key: `study-library-admin-mock-db`
- Persistent demo data is centralized in `src/mock/db.ts`
- If you add a new admin module with demo data, extend `MockDatabase` and seed data in `src/mock/db.ts`
- Prefer logging mock-side mutations with `appendOperationLog(...)`
- Prefer logging auth transitions with `appendLoginLog(...)`
- Keep mock data shapes close to documented backend interface shapes, then adapt them in the adapter layer if needed.

## Adapter Layer Rules
- Response normalization belongs in `src/api/adapters/admin.ts`
- Promise response wrapping belongs in `src/api/utils/adapter.ts`
- When adding or changing API fields, prefer:
  1. backend/raw shape in API response
  2. adapter normalization to page shape
  3. page component consumes normalized shape only
- Avoid scattering `userId`/`courseId`/`bankId` to `id` conversion logic inside page components.
- If request payload shape differs from UI form shape, add a serializer in `src/api/adapters/admin.ts`.

## Adding a New Admin Module
- Add or update the page component under `src/pages/...`
- Add API module under `src/api/modules/...`
- Add mock module under `src/mock/modules/...` if mock mode should support it
- Extend `src/mock/db.ts` if the module needs persistent demo data
- Add menu entry in `src/router/utils/routerConfig.ts` if it should appear in the sidebar
- Add locale keys in both `src/languageApi/locales/zh.json` and `src/languageApi/locales/en.json`
- Add a matching SVG icon under `src/assets/images/svg`
- If the module introduces statuses or filters, centralize them in `src/constants/enums.ts`

## Code Style
### Imports
- Use `@/` aliases for app-local modules.
- Group imports as framework, third-party, then app-local.
- Use `import type` for type-only imports.
- Keep semicolons.

### Formatting
- Use 2-space indentation.
- Prefer single quotes.
- Respect Prettier settings in `.prettierrc.json`.
- Keep SFC order as `template`, `script setup`, `style`.

### Vue Patterns
- Use `<script setup lang="ts">`.
- Prefer `ref`, `reactive`, `computed`, `onMounted`.
- Keep page-level logic in the page file unless shared by multiple modules.
- Use Element Plus components consistently with current page patterns.

### Types
- `strict` TypeScript is enabled.
- Avoid introducing new `any` unless absolutely necessary.
- Prefer explicit record types from `src/types/admin.ts`.
- If a new module adds new entities, define types first, then API, then page.

### Naming
- Page folders use lower camel case, e.g. `userManage`, `questionBankManage`
- API exports use `*Http`, e.g. `userHttp`, `reportHttp`
- Mock exports use `*Mock`
- Route `name` values follow page folder names when possible

## Routing and Navigation
- Sidebar menus come from `src/router/utils/routerConfig.ts`
- Menu text should use `localeKey` and language packs
- Route title changes should keep `meta.title` and `meta.localeKey` aligned
- Secondary detail pages can live outside sidebar menus but should still be routed in `src/router/index.ts`
- If a page should appear in visited tabs, ensure it has a stable `name` and `meta.title`

## UI Expectations
- Preserve the current admin look: light background, dark sidebar, clean cards
- Reuse `OverviewCard`, `BreadcrumbBar`, `RouteTabs`, and `MenuIcon` where relevant
- Use `page-shell`, `page-card`, `page-title`, and `page-desc` helpers for consistency
- Prefer real tables/forms/drawers/dialogs over placeholder pages for main modules

## Documentation Links
- Subproject overview: `study-library-admin/README.md`
- Backend integration checklist: `docs/需求设计文档/学习通后台前后端对接清单.md`
- Real backend switch guide: `docs/需求设计文档/学习通后台真实接口切换与联调步骤.md`
- Module-by-module integration checklist: `docs/需求设计文档/学习通后台模块级联调任务拆解清单.md`
- Integration execution board: `docs/需求设计文档/学习通后台联调执行看板模板.md`
- Integration issue template: `docs/需求设计文档/学习通后台联调问题记录模板.md`
- Integration acceptance checklist: `docs/需求设计文档/学习通后台联调验收检查表.md`
- Delivery and acceptance checklist: `docs/需求设计文档/学习通后台交付与验收清单.md`
- Master interface design: `docs/需求设计文档/学习通接口清单.md`

## Before Finishing Work
- Check whether you also need to update:
  - `src/router/utils/routerConfig.ts`
  - `src/languageApi/locales/zh.json`
  - `src/languageApi/locales/en.json`
  - `src/constants/enums.ts`
  - `src/mock/db.ts`
  - `study-library-admin/README.md`
- If you changed response shapes, confirm adapters still normalize correctly.
- If you added a persistent mock module, make sure edits survive page refresh in mock mode.
