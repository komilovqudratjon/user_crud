package com.example.backent.Test;

public class Person {
  private String name;
  private long age;

  private String gender;

  public Person(String name, long age, String gender) {
    this.name = name;
    this.age = age;
    this.gender = gender;
  }

  public String getGender() {
    return gender;
  }

  public String getName() {
    return name;
  }

  public long getAge() {
    return age;
  }
}
