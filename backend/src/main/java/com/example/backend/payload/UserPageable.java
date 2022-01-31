package com.example.backend.payload;

import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
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
