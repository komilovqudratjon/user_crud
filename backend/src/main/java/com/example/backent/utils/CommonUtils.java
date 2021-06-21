package com.example.backent.utils;

import com.example.backent.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CommonUtils {
  public static void validatePageNumberAndSize(int page, int size) {
    if (page < 0) {
      throw new BadRequestException("Sahifa soni noldan kam bo'lishi mumkin emas.");
    }

    if (size > AppConstants.MAX_PAGE_SIZE) {
      throw new BadRequestException(
          "Sahifa soni " + AppConstants.MAX_PAGE_SIZE + " dan ko'p bo'lishi mumkin emas.");
    }
  }

  public static Pageable getPageable(int page, int size) {
    validatePageNumberAndSize(page, size);
    return PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
  }

  public static Pageable getPageableById(int page, int size) {
    validatePageNumberAndSize(page, size);
    return PageRequest.of(page, size, Sort.Direction.DESC, "id");
  }

  public static Timestamp parseTimestamp(String timestamp) {
    SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    try {
      return new Timestamp(DATE_TIME_FORMAT.parse(timestamp).getTime());
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
