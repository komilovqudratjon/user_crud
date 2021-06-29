package com.example.backent.Test;

import com.example.backent.entity.enums.RoleName;

public class Test {
  public static void main(String[] args) {

    System.out.println(RoleName.values().length);
    for (RoleName value : RoleName.values()) {
      System.out.println(value);
    }
  }
}
