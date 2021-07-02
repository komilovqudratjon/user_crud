package com.example.backent.Test;

import com.example.backent.entity.enums.RoleName;

public class Test {
  public static void main(String[] args) {
    String str = "hello world!";

    // capitalize first letter
    String output = str.substring(0, 1).toUpperCase() + str.substring(1);

    // print the string
    System.out.println(output);
  }
}
