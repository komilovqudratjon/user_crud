package com.example.backent.entity;

import com.example.backent.entity.template.AbsEntity;
import com.sun.istack.NotNull;
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

  private String firstname;
  private String lastname;
  private String middlename;
  private String address;

  @Column(unique = true)
  private String passportNumber;

  private Date dateOfBirth;

  @Column(unique = true)
  private String phoneNumber;

  @Column(unique = true)
  @NotNull
  private String email;

  @ManyToMany private List<FieldsForUsers> fields;

  @ManyToMany private List<UserExperience> experiences;

  @ManyToMany private List<UsersLanguage> languages;

  @ManyToMany private List<ProgramingLanguage> programingLanguages;

  @NotNull private String password;

  @OneToOne(fetch = FetchType.LAZY)
  private Attachment avatar;

  public User(
      String firstname,
      String lastname,
      String middlename,
      String address,
      String passportNumber,
      Date dateOfBirth,
      String phoneNumber,
      String email,
      List<FieldsForUsers> fields,
      List<UserExperience> experiences,
      List<UsersLanguage> languages,
      List<ProgramingLanguage> programingLanguages,
      String password,
      List<Role> roles) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.middlename = middlename;
    this.address = address;
    this.passportNumber = passportNumber;
    this.dateOfBirth = dateOfBirth;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.fields = fields;
    this.experiences = experiences;
    this.languages = languages;
    this.programingLanguages = programingLanguages;
    this.password = password;
    this.roles = roles;
  }

  private boolean active = true;

  private boolean deleted = false;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_role",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private List<Role> roles;

  private boolean accountNonExpired = true;
  private boolean accountNonLocked = true;
  private boolean credentialsNonExpired = true;
  private boolean enabled = true;

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
