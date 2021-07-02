package com.example.backent.entity;

import com.example.backent.entity.enums.Family;
import com.example.backent.entity.enums.WorkTimeType;
import com.example.backent.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbsEntity implements UserDetails {

  @Column(nullable = false)
  private String firstname; // REQUIRED

  @Column(nullable = false)
  private String lastname; // REQUIRED

  private String middlename;

  private String address;

  @Enumerated(EnumType.STRING)
  private WorkTimeType workTimeType; // REQUIRED

  @Enumerated(EnumType.STRING)
  private Family family;

  @Column(unique = true)
  private String passportNumber;

  private Date dateOfBirth;

  private Date startWorkingTime;

  @Column(nullable = false, unique = true)
  private String phoneNumber; // REQUIRED

  @Column(nullable = false, unique = true)
  private String email; // REQUIRED

  @Column(nullable = false)
  @ManyToMany
  private List<FieldsForUsers> fields; // REQUIRED

  @ManyToMany private List<UserExperience> experiences;

  @ManyToMany private List<UsersLanguage> languages;

  @ManyToMany private List<ProgramingLanguage> programingLanguages;

  @Column(nullable = false)
  @Transient
  private String password; // REQUIRED

  @OneToOne(fetch = FetchType.LAZY)
  private Attachment avatar;

  public User(
      String firstname,
      String lastname,
      String middlename,
      String address,
      WorkTimeType workTimeType,
      Family family,
      String passportNumber,
      Date dateOfBirth,
      Date startWorkingTime,
      String phoneNumber,
      String email,
      List<FieldsForUsers> fields,
      List<UserExperience> experiences,
      List<UsersLanguage> languages,
      List<ProgramingLanguage> programingLanguages,
      String password,
      boolean active,
      List<Role> roles,
      Attachment avatar) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.middlename = middlename;
    this.address = address;
    this.workTimeType = workTimeType;
    this.family = family;
    this.passportNumber = passportNumber;
    this.dateOfBirth = dateOfBirth;
    this.startWorkingTime = startWorkingTime;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.fields = fields;
    this.experiences = experiences;
    this.languages = languages;
    this.programingLanguages = programingLanguages;
    this.password = password;
    this.active = active;
    this.roles = roles;
    this.avatar = avatar;
  }

  private boolean active = true;

  private boolean deleted = false;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_role",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private List<Role> roles;

  @Transient private boolean accountNonExpired = true;
  @Transient private boolean accountNonLocked = true;
  @Transient private boolean credentialsNonExpired = true;
  @Transient private boolean enabled = true;

  public User(String email, String password, String firstname, String lastname, List<Role> roles) {
    this.email = email;
    this.password = password;
    this.firstname = firstname;
    this.lastname = lastname;
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }
}
