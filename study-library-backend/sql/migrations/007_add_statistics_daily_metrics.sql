CREATE TABLE IF NOT EXISTS statistics_daily_metrics (
  metric_date DATE PRIMARY KEY,
  new_users INT NOT NULL DEFAULT 0,
  active_users INT NOT NULL DEFAULT 0,
  course_views INT NOT NULL DEFAULT 0,
  content_interactions INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NULL,
  updated_at TIMESTAMP NULL,
  created_by VARCHAR(64) NULL,
  updated_by VARCHAR(64) NULL,
  deleted BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT IGNORE INTO statistics_daily_metrics (metric_date, new_users, active_users, course_views, content_interactions)
VALUES
  ('2026-04-24', 8, 92, 240, 18),
  ('2026-04-25', 11, 98, 268, 24),
  ('2026-04-26', 13, 106, 301, 31),
  ('2026-04-27', 15, 114, 326, 37),
  ('2026-04-28', 12, 109, 315, 34),
  ('2026-04-29', 17, 121, 348, 42),
  ('2026-04-30', 19, 133, 372, 49);
