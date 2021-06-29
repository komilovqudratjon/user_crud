package com.example.backent.repository;

import com.example.backent.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
//    ticketlarni project type va frontvyoki back end buyicha search qiladi
    @Query(value = "select * from ticket where project_type_id =:id and work_type=:type and tag_id=:tagId",nativeQuery = true)
    List<Ticket> findAllByTypeAndWorkType(@Param("id") Long id,@Param("type") String type,@Param("tagId") Long tagId);

    @Query(value = "select * from ticket where board_id in (select board.id from board where project_id in (select project.id from project where project.id=:id)) and ticket_condition='CREATED'",nativeQuery = true)
    List<Ticket> backlog(@Param("id") Long id);

    @Query(value = "select count(hours_worker) from ticket where board_id in (select board.id from board where board_condition='DONE' and project_id in (select project.id from project where project.id=:id))",nativeQuery = true)
    Long findDoneTicket(@Param("id") Long id);

    @Query(value = "select count(hours_worker) from ticket where board_id in (select board.id from board where project_id in (select project.id from project where project.id=:id))",nativeQuery = true)
    Long allTicketByProject(@Param("id") Long id);
}
