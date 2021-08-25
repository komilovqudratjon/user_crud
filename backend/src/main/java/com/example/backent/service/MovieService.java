package com.example.backent.service;

import com.example.backent.payload.MovieModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 24/08/21
 */
@Service
@Slf4j
public class MovieService {

  private final RestTemplate restTemplate;

  public MovieService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Async
  public CompletableFuture lookForMovie(String movieId) throws InterruptedException {
    log.info("Looking up Movie ID: {}", movieId);
    String url = String.format("https://ghibliapi.herokuapp.com/films/%s", movieId);
    MovieModel results = restTemplate.getForObject(url, MovieModel.class);
    // Artificial delay of 1s for demonstration purposes
    Thread.sleep(5000L);
    return CompletableFuture.completedFuture(results);
  }
}
