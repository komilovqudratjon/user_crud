package com.example.backent.payload;

import java.util.List;

import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public @Getter @Setter @AllArgsConstructor @NoArgsConstructor class UserPageable {
  private int number;
  private boolean last;
  private int size;
  private int numberOfElements;
  private int totalPages;
  private Pageable pageable;
  private Sort sort;
  private boolean first;
  private int totalElements;
  private boolean empty;
  private List<ReqUser> content;
}
