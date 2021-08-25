package com.example.backent.component;

import com.example.backent.payload.MovieModel;
import com.example.backent.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 24/08/21
 */
@Component
@Slf4j
public class ApplicationRunner implements CommandLineRunner {

  private final MovieService movieService;

  public ApplicationRunner(MovieService movieService) {
    this.movieService = movieService;
  }

  @Override
  public void run(String... args) throws Exception {
    // Start the clock
    long start = System.currentTimeMillis();

    // Kick of multiple, asynchronous lookups
    CompletableFuture page1 = movieService.lookForMovie("58611129-2dbc-4a81-a72f-77ddfc1b1b49");
    CompletableFuture page2 = movieService.lookForMovie("2baf70d1-42bb-4437-b551-e5fed5a87abe");
    CompletableFuture page3 = movieService.lookForMovie("4e236f34-b981-41c3-8c65-f8c9000b94e7");

    // Join all threads so that we can wait until all are done
    CompletableFuture.allOf(page1, page2, page3).join();

    // Print results, including elapsed time
    log.info("Elapsed time: " + (System.currentTimeMillis() - start));
    log.info("--> " + page1.get());
    log.info("--> " + page2.get());
    log.info("--> " + page3.get());
  }
}
