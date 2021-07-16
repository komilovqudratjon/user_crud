package com.example.backent.payload;

import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPageable {
  private int number;
  private boolean last;
  private int size;
  private int numberOfElements;
  private int totalPages;
  private Pageable pageable;
  private Sort sort;
  private boolean first;
  private long totalElements;
  private boolean empty;
  private List<ReqUser> content;
}
