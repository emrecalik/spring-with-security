package com.springsecurity.poll.controller;

import com.springsecurity.poll.model.response.PollResponseDTO;
import com.springsecurity.poll.model.response.UserIdentityAvailabilityResponseDTO;
import com.springsecurity.poll.model.response.UserProfileResponseDTO;
import com.springsecurity.poll.model.response.UserSummaryResponseDTO;
import com.springsecurity.poll.security.MyUserPrincipal;
import com.springsecurity.poll.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserSummaryResponseDTO getCurrentUser(@AuthenticationPrincipal MyUserPrincipal myUserPrincipal) {
        UserSummaryResponseDTO userSummary = new UserSummaryResponseDTO();
        userSummary.setId(myUserPrincipal.getId());
        userSummary.setName(myUserPrincipal.getName());
        userSummary.setUserName(myUserPrincipal.getUsername());
        return userSummary;
    }

    @GetMapping("/user/checkUserNameAvailability")
    public UserIdentityAvailabilityResponseDTO checkUserNameAvailability(@RequestParam(value = "username") String userName) {
        return userService.checkUserNameAvailability(userName);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailabilityResponseDTO checkEmailAvailability(@RequestParam(value = "email") String email) {
        return userService.checkEmailAvailability(email);
    }

    @GetMapping("/users/{userName}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponseDTO getUserProfile(@PathVariable String userName) {
        return userService.getUserProfile(userName);
    }

    @GetMapping("/users/{userName}/polls")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<PollResponseDTO> getPollsCreatedBy(@PathVariable String userName) {
        return userService.getPollsCreatedBy(userName);
    }

    @GetMapping("/users/{userName}/votes")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<PollResponseDTO> getPollsVotedBy(@PathVariable String userName) {
        return userService.getPollsVotedBy(userName);
    }
}
