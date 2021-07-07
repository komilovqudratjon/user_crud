package com.example.backent.Test;

import com.example.backent.entity.enums.RoleName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
  public static void main(String[] args) {

    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    System.out.println(date);
  }
}
