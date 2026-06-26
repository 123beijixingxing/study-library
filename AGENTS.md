# AGENTS.md
## Scope
- This repo is mostly documentation plus one real app under `docs/demo/financial-expense-system`.
- Treat `docs/demo/financial-expense-system` as the primary codebase for builds, checks, and source edits.
- Root `docs/需求设计文档` contains generated/derived requirement docs; edit those only for documentation tasks.
- Do not edit `node_modules`, `.history`, or generated declaration files unless explicitly asked.

## Rule Files
- No prior root `AGENTS.md` was present.
- No Cursor rules were found in `.cursor/rules/`.
- No `.cursorrules` file was found.
- No Copilot instructions were found in `.github/copilot-instructions.md`.

## Repo Layout
- App source: `docs/demo/financial-expense-system/src`
- Public assets: `docs/demo/financial-expense-system/public`
- Build config: `docs/demo/financial-expense-system/vite.config.ts`
- TS config: `docs/demo/financial-expense-system/tsconfig*.json`
- ESLint config: `docs/demo/financial-expense-system/.eslintrc.ts`
- Prettier config: `docs/demo/financial-expense-system/.prettierrc.json`
- Pages: `docs/demo/financial-expense-system/src/pages`
- Router: `docs/demo/financial-expense-system/src/router`
- API: `docs/demo/financial-expense-system/src/api`
- Shared utilities: `docs/demo/financial-expense-system/src/utils`
- Vuex store: `docs/demo/financial-expense-system/src/store/index.ts`

## Package Manager
- Prefer `yarn`; `yarn.lock` is checked in.
- Run app commands from `docs/demo/financial-expense-system`.

## Build / Type-Check / Lint / Test Commands
### Install
```bash
yarn
```

### Dev Server
```bash
yarn dev
```
- Vite serves on port `9999` by default.

### Production Build
```bash
yarn build
```
- Script: `vue-tsc -b && vite build`.
- Current status: fails on repo type errors.
- Observed failures include missing `ComponentInternalInstance` imports in many page SFCs and unresolved path/type issues in `src/api/utils/apiConfig.ts`.

### Preview Build
```bash
yarn preview
```

### Standalone Type Check
```bash
npx vue-tsc -b
```
- Use this for TS diagnostics without bundling.

### Lint
```bash
npx eslint src --ext .ts,.vue
```
- There is no `lint` script in `package.json`.
- Current status: this fails because ESLint is v9 but the repo only has legacy `.eslintrc.ts`; there is no `eslint.config.js`.
- The checked-in config also references missing direct deps such as `eslint-plugin-vue`, `@typescript-eslint/*`, and Prettier ESLint bridge packages.
- Treat lint setup repair as its own change; do not silently “fix lint” during unrelated work.

### Format
```bash
npx prettier --check "src/**/*.{ts,vue,scss,less,json}"
npx prettier --write "src/**/*.{ts,vue,scss,less,json}"
```
- There is no format script; run Prettier directly.

### Tests
- No working first-party test command is configured.
- No repo-owned `tests/`, `*.spec.*`, or `*.test.*` files were found outside dependencies.
- `tsconfig` includes Mocha/Chai types, but there is no runnable harness checked in.

### Single Test
- Not available right now because the project has no first-party tests or test runner.
- If tests are added later, document the exact file-scoped command here.

## Generated / Derived Files
- Do not hand-edit `docs/demo/financial-expense-system/components.d.ts`.
- Do not hand-edit `docs/demo/financial-expense-system/src/auto-imports.d.ts`.
- Regenerate them via tooling if needed.

## Architecture Notes
- Stack: Vue 3 + TypeScript + Vite.
- SFC style: mostly `<script setup lang="ts">`.
- UI libs: Element Plus and Vant.
- Routing uses `createWebHashHistory()`.
- Shared app state is primarily Vuex, even though a Pinia plugin exists.
- HTTP requests go through `src/utils/Request.ts`.
- API modules are aggregated from `src/api/modules` via `import.meta.glob` in `src/api/index.ts`.
- Route metadata lives in `src/router/utils/routerConfig.ts`.

## Code Style Guidelines
### Imports
- Prefer `@/` aliases for app-local modules under `src`.
- Use relative imports only for tightly local files in the same feature or plugin folder.
- Group imports as framework, third-party, then app-local.
- Use `import type` for type-only imports when practical.
- Preserve the existing semicolon-terminated import style.

### Formatting
- Follow 2-space indentation in TS, Vue scripts, and styles.
- Prefer single quotes in TS/JS.
- Keep semicolons; source files and Prettier both lean semicolon-on even though ESLint does not enforce it.
- Keep lines reasonable; Prettier `printWidth` is `150`.
- Use trailing commas where Prettier would add them (`es5`).
- Keep object spacing like `{ foo: bar }`.

### Vue SFCs
- Keep file order as `template`, then `script setup`, then `style`.
- Use `lang="ts"` in script blocks.
- Follow the current Composition API pattern with `reactive`, `ref`, `computed`, and `watch`.
- Existing code uses `defineExpose`; only expose members that parents actually need.
- Preserve current Element Plus template and slot conventions when editing existing screens.

### Naming
- Preserve existing naming instead of forcing a new scheme.
- Page folders and route names commonly use lower camel case, e.g. `paymentApplication`, `voucherList`, `loginForm`.
- Utility classes use PascalCase in camel-case files, e.g. `class Base` in `base.ts`.
- API exports commonly use the `*Http` suffix, e.g. `loginHttp`.
- Vuex mutations and actions follow `setXxx` naming.

### Types
- `strict` TypeScript is enabled; prefer explicit interfaces and return types for new code.
- Avoid expanding existing `any` usage unless compatibility requires it.
- When touching a module that already uses `any`, improve nearby types if the change is low-risk.
- Import missing framework types explicitly; do not assume globals such as `ComponentInternalInstance` exist.
- Browser globals like `window` are fine in app code, but keep files inside the app tsconfig scope so DOM libs apply.

### State Management
- Prefer the existing Vuex store for shared state.
- Do not introduce new Pinia stores unless the task is explicitly a migration.
- Reuse `src/utils/saveStorage.ts` for persisted session/local storage behavior.

### API Layer
- Add new backend calls in `src/api/modules/*.ts` and let `src/api/index.ts` aggregate them.
- Reuse `apiConfig.ts` helpers such as `apiHeader`, `apiHeaderTest`, and `rest(...)`.
- Route all HTTP traffic through `src/utils/Request.ts`; do not create raw Axios instances in feature files.
- Preserve the existing request shape: `{ params, data, headers, notRepeatApi, ...other }`.

### Error Handling
- Match the centralized error flow in `src/utils/Request.ts`.
- Prefer user-visible feedback through `ElMessage` or the project’s global message helpers.
- Handle session expiry by clearing relevant store state and redirecting to `login`.
- Do not swallow promise rejections silently unless the UX intentionally tolerates failure.

### Logging
- The codebase already contains many `console.log` calls.
- Avoid adding new debug logs unless they materially help the task.
- Remove temporary logs from production-sensitive paths before finishing.

### Routing
- Keep new route records aligned with `routerConfig.ts` metadata shape.
- Preserve `meta.keepAlive`, titles, ordering, and permission-related fields when adding routes.
- The app uses a catch-all route that redirects unknown paths to login; keep that behavior unless auth flow changes.

### i18n / Text
- Reuse existing language helpers such as `proxy.$tm`, `proxy.$i18n`, and `src/utils/language.ts` where a screen is already localized.
- Avoid hardcoding new UI strings in components that already rely on `pageText` or locale packs.
- Locale JSON files live under `src/languageApi/locales`; public language pack assets live under `public/static/languagePack`.

### Styles and Assets
- Shared global styles are imported in `src/App.vue` from `src/assets/style`.
- Page-level styles frequently use scoped Less; match the local file’s preprocessor.
- Reuse helpers like `base.getImgFile(...)` for image paths when working in current screens.
- Do not casually rename or move asset files; many templates reference them directly.

## Agent Workflow Tips
- For app work, search and edit inside `docs/demo/financial-expense-system` unless the task explicitly targets documentation.
- Ignore dependency tests in `node_modules`; they are not part of the project.
- If lint or test infrastructure needs improvement, make that a focused change and update this file with the new commands.
- Before claiming a build or lint fix, rerun the relevant command from the app directory.
