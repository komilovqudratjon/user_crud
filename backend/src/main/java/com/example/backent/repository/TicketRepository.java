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
  @Query(
      value =
          "select * from ticket where project_type_id =:id and work_type=:type and tag_id=:tagId",
      nativeQuery = true)
  List<Ticket> findAllByTypeAndWorkType(
      @Param("id") Long id, @Param("type") String type, @Param("tagId") Long tagId);

  @Query(
      value =
          "select * from ticket where board_id in (select board.id from board where condition= :condition and project_id in (:id))",
      nativeQuery = true)
  List<Ticket> selectByConditionAndProjectId(
      @Param("id") Long id, @Param("condition") String condition);

  @Query(
      value =
          "select * from ticket where board_id in (select board.id from board where project_id in (:id)) and ticket_condition='CREATED'",
      nativeQuery = true)
  List<Ticket> getBacklog(@Param("id") Long id);

  @Query(
      value =
          "select sum(hours_worker) from ticket where board_id in (select board.id from board where board.condition='DONE' and project_id in (:id))",
      nativeQuery = true)
  Long findDoneTicket(@Param("id") Long id);

  @Query(
      value =
          "select count(hours_worker) from ticket where board_id in (select board.id from board where project_id in (:id))",
      nativeQuery = true)
  Long allTicketByProject(@Param("id") Long id);

  @Query(
      value =
          "select sum(hours_worker) from ticket where work_type=:type and board_id in (select board.id from board where project_id in (:id))",
      nativeQuery = true)
  Double countAllTicketByWorkType(@Param("type") String type, @Param("id") Long id);

  @Query(
      value =
          "select sum(hours_worker) from ticket where work_type=:type and board_id in (select board.id from board where project_id in (:id) and condition='DONE')",
      nativeQuery = true)
  Double countDoneTicketByWorkType(@Param("type") String type, @Param("id") Long id);

  List<Ticket> findAllByBoardId(Long board_id);
}
