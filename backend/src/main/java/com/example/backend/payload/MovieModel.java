package com.example.backend.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 24/08/21
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieModel {
  private String title;
  private String producer;

  // standard getters and setters

  @Override
  public String toString() {
    return String.format("MovieModel{title='%s', producer='%s'}", title, producer);
  }
}
