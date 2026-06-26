package com.studylib.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class DateTimeUtils {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final List<DateTimeFormatter> SUPPORTED_FORMATTERS = List.of(
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
  );

  private DateTimeUtils() {
  }

  public static LocalDateTime now() {
    return LocalDateTime.now();
  }

  public static String nowText() {
    return now().format(FORMATTER);
  }

  public static LocalDateTime parseDateTimeOrNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    String normalized = value.trim().replace('T', ' ');
    if (normalized.endsWith("Z")) {
      normalized = normalized.substring(0, normalized.length() - 1);
    }

    for (DateTimeFormatter formatter : SUPPORTED_FORMATTERS) {
      try {
        return LocalDateTime.parse(normalized, formatter);
      } catch (DateTimeParseException ignored) {
      }
    }

    return null;
  }

  public static LocalDateTime parseDateTimeOrNow(String value) {
    LocalDateTime parsed = parseDateTimeOrNull(value);
    return parsed == null ? now() : parsed;
  }

  public static String normalizeDateTimeText(String value) {
    if (value == null || value.isBlank()) {
      return "";
    }

    String normalized = value.trim().replace('T', ' ');
    if (normalized.endsWith("Z")) {
      normalized = normalized.substring(0, normalized.length() - 1);
    }

    for (DateTimeFormatter formatter : SUPPORTED_FORMATTERS) {
      try {
        return LocalDateTime.parse(normalized, formatter).format(FORMATTER);
      } catch (DateTimeParseException ignored) {
      }
    }

    return normalized;
  }

  public static String normalizeOrNow(String value) {
    String normalized = normalizeDateTimeText(value);
    return normalized.isBlank() ? nowText() : normalized;
  }
}
