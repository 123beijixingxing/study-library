# SQL Migrations

Put incremental schema changes here after the baseline init scripts in `sql/init` are applied.

Suggested conventions:

- Use ascending numeric prefixes, e.g. `007_add_xxx.sql`
- Prefer one focused schema concern per file
- When introducing a new business table, align with `docs/architecture/persistence-conventions.md`
- If a time column represents a real timestamp, prefer `TIMESTAMP` over `VARCHAR(32)`
- If the table supports admin deletion semantics, add `deleted BOOLEAN NOT NULL DEFAULT FALSE`
