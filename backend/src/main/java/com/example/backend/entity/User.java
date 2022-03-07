package com.example.backend.entity;

import com.example.backend.entity.enums.Gender;
import com.example.backend.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname; // REQUIRED

    @Column(nullable = false)
    private String lastname; // REQUIRED

    private String middleName;

    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender; // REQUIRED

    private String nation;

    private String addressOfBirth;

    @Column(nullable = false)
    private String citizenship; // fiqaroligi

    @Column
    private Date passportGivenTime;

    @Column
    private String passportWhoGave;

    @Column
    private String pinfl;

    @Column(nullable = false)
    private String phoneNumber; // REQUIRED

    @OneToOne
    private Attachment photo;

    @Column(columnDefinition = "TEXT")
    private String currentStatus;

    @Column(columnDefinition = "TEXT")
    private String susceptibilityToDisease;

    @Column(columnDefinition = "TEXT")
    private String propensityToAssassinate;

    @Column(columnDefinition = "TEXT")
    private String weaknessesAndStrengths;

    @Column(columnDefinition = "TEXT")
    private String socialResponsibility;

    @Column(columnDefinition = "TEXT")
    private String positionToConform;

    @ManyToOne
    private Attachment anotherPhotos;

//    private Boolean aBoolean;


    public User(String firstname,
                String lastname,
                String middleName,
                Date dateOfBirth,
                Gender gender,
                String nation,
                String addressOfBirth,
                String citizenship,
                Date passportGivenTime,
                String passportWhoGave,
                String pinfl,
                String phoneNumber,
                Attachment photo) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.middleName = middleName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nation = nation;
        this.addressOfBirth = addressOfBirth;
        this.citizenship = citizenship;
        this.passportGivenTime = passportGivenTime;
        this.passportWhoGave = passportWhoGave;
        this.pinfl = pinfl;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }
}
