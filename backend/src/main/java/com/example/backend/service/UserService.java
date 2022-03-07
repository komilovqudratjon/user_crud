package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.payload.ApiResponseModel;
import com.example.backend.payload.ErrorsField;
import com.example.backend.payload.ReqUser;
import com.example.backend.payload.UserPageable;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Service
public class UserService {
    private final UserRepository userRepository;


    @Autowired
    public UserService(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public HttpEntity<?> getPageable(
            Optional<Integer> page,
            @Valid Optional<String> sortType, Optional<Integer> size,
            Optional<String> sortBy,
            Optional<String> search) {

        Sort.Direction sort = sortType.orElse("DESC").equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        try {
            UserPageable userPageable = new UserPageable();
            Page<User> id;

            if (search.isEmpty()) {
                id = userRepository.findAll(PageRequest.of(
                        page.orElse(0), size.orElse(5), sort, sortBy.orElse("id")));
            } else {

                String s = search.get();
                id = userRepository
                        .findAllByFirstnameIgnoreCaseContainingOrLastnameIgnoreCaseContainingOrMiddleNameIgnoreCaseContainingOrAddressOfBirthIgnoreCaseContainingOrPinflIgnoreCaseContaining(
                                s,
                                s,
                                s,
                                s,
                                s,
                                PageRequest.of(
                                        page.orElse(0), size.orElse(5), sort, sortBy.orElse("id")));

            }
            userPageable.setPageable(id.getPageable());
            userPageable.setEmpty(id.isEmpty());
            userPageable.setSort(id.getSort());
            userPageable.setFirst(id.isFirst());
            userPageable.setLast(id.isLast());
            userPageable.setNumber(id.getNumber());
            userPageable.setSize(id.getSize());
            userPageable.setTotalElements(id.getTotalElements());
            userPageable.setTotalPages(id.getTotalPages());
            userPageable.setNumberOfElements(id.getNumberOfElements());
            userPageable.setContent(
                    id.stream()
                            .map(
                                    this::makeReqUser
                            )
                            .collect(Collectors.toList()));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseModel(HttpStatus.OK.value(), "users", userPageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiResponseModel(
                                    HttpStatus.CONFLICT.value(),
                                    "The request is incorrect",
                                    List.of(new ErrorsField("error", e.getMessage()))));
        }
    }


    public HttpEntity<?> getUserById(Long id) {
        try {
            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                User user = byId.get();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(
                                new ApiResponseModel(
                                        HttpStatus.OK.value(),
                                        "user",
                                        makeReqUser(user)));
            }
            return ResponseEntity.badRequest()
                    .body(
                            new ApiResponseModel(
                                    HttpStatus.CONFLICT.value(),
                                    "not found user",
                                    List.of(new ErrorsField("error", null))));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiResponseModel(
                                    HttpStatus.CONFLICT.value(),
                                    "The request is incorrect",
                                    List.of(new ErrorsField("error", e.getMessage()))));
        }
    }

    public ReqUser makeReqUser(User user) {
        return new ReqUser(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getMiddleName(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getNation(),
                user.getAddressOfBirth(),
                user.getCitizenship(),
                user.getPassportGivenTime(),
                user.getPassportWhoGave(),
                user.getPinfl(),
                user.getPhoneNumber(),
                user.getPhoto() == null
                        ? null
                        : ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/attach/")
                        .path(user.getPhoto().getId().toString())
                        .toUriString(),
                user.getCurrentStatus(),
                user.getSusceptibilityToDisease(),
                user.getPropensityToAssassinate(),
                user.getWeaknessesAndStrengths(),
                user.getSocialResponsibility(),
                user.getPositionToConform(),
                user.getAnotherPhotos() == null || user.getAnotherPhotos().size() == 0
                        ? null
                        : user.getAnotherPhotos().stream().map(attachment ->
                        ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/api/attach/")
                                .path(attachment.getId().toString())
                                .toUriString()).collect(Collectors.toList())
        );
    }
}
