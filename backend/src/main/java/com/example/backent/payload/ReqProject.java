package com.example.backent.payload;

import com.example.backent.entity.enums.ProjectSTATUS;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqProject {

  private Long id;
  private String name; //
  private Long companyId; //

  @ApiModelProperty(
      notes = "time play day",
      name = "time play day",
      required = true,
      value = "2021-03-03",
      example = "2021-03-03")
  @Pattern(
      regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$",
      message = "startDate is invalid")
  private String startDate; //

  @ApiModelProperty(
      notes = "time play day",
      name = "time play day",
      required = true,
      value = "2021-03-03",
      example = "2021-03-03")
  @Pattern(
      regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$",
      message = "endDate is invalid")
  private String endDate; //

  private List<Long> agreements;
  private List<Long> users;
  private ProjectSTATUS projectType; //
  private Long responsible; //
}
