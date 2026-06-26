# Frontend Skeleton

This folder contains a lightweight Vite + TypeScript frontend skeleton derived from the Study Library API documents.

Suggested structure highlights:
- `package.json`: Vite + TypeScript baseline
- `src/api/`: request wrapper and API modules
- `src/types/`: DTO/VO-aligned TypeScript types
- `src/constants/`: enums and error-code constants
- `src/config.ts`: environment-based runtime config

How to use:
1. Copy this skeleton into a real frontend repository.
2. Replace the placeholder renderer with Vue or React app entry.
3. Plug `request.ts` into Axios or your existing request client.
4. Keep enums and error-code handling aligned with backend constants and OpenAPI draft.

Suggested next steps:
- Replace the placeholder renderer with Vue or React entry files
- Plug the generated `api/` and `types/` files into your request client
- Align enums and error handling with the backend error-code constants
