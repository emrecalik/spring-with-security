package com.springsecurity.poll.bootstrap;

import com.springsecurity.poll.domain.*;
import com.springsecurity.poll.repository.PollRepository;
import com.springsecurity.poll.repository.RoleRepository;
import com.springsecurity.poll.repository.UserRepository;
import com.springsecurity.poll.repository.VoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PollRepository pollRepository;
    VoteRepository voteRepository;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository,
                      PollRepository pollRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        Role adminRole = new Role();
        adminRole.setRoleName(RoleName.ROLE_ADMIN);

        Role userRole = new Role();
        userRole.setRoleName(RoleName.ROLE_USER);

        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        User emre = new User();
        emre.setEmail("emre.calik01@gmail.com");
        emre.setName("Emre Çalık");
        emre.setUserName("emreboun");
        emre.setPassword("emre1905");
        emre.setRoles(Set.of(adminRole, userRole));

        User kaan = new User();
        kaan.setEmail("kaan.can@tei.com.tr");
        kaan.setName("Kaan Can");
        kaan.setUserName("kaancan");
        kaan.setPassword("kaan.1989");
        kaan.setRoles(Set.of(userRole));

        User yusuf = new User();
        yusuf.setEmail("yusuf.gokyer@ge.com.tr");
        yusuf.setName("Yusuf");
        yusuf.setUserName("yusufboun");
        yusuf.setPassword("yusuf.1988");
        yusuf.setRoles(Set.of(userRole));

        userRepository.save(emre);
        userRepository.save(kaan);
        userRepository.save(yusuf);

        Choice javaChoice = new Choice();
        javaChoice.setTitle("Java");

        Choice pythonChoice = new Choice();
        pythonChoice.setTitle("Python");

        Choice javaScriptChoice = new Choice();
        javaScriptChoice.setTitle("JavaScript");

        Poll favoriteLanguage = new Poll();
        favoriteLanguage.setQuestion("What is your favorite software language?");
        favoriteLanguage.setChoices(List.of(javaChoice, pythonChoice, javaScriptChoice));

        javaChoice.setPoll(favoriteLanguage);
        pythonChoice.setPoll(favoriteLanguage);
        javaScriptChoice.setPoll(favoriteLanguage);

        favoriteLanguage.setCreatedBy(1L);
        favoriteLanguage.setExpiredAt(Instant.now().plusSeconds(86400));

        pollRepository.save(favoriteLanguage);

        Vote vote1 = new Vote();
        vote1.setUser(emre);
        vote1.setChoice(javaChoice);
        vote1.setPoll(favoriteLanguage);

        Vote vote2 = new Vote();
        vote2.setUser(kaan);
        vote2.setChoice(pythonChoice);
        vote2.setPoll(favoriteLanguage);

        Vote vote3 = new Vote();
        vote3.setUser(yusuf);
        vote3.setChoice(javaChoice);
        vote3.setPoll(favoriteLanguage);

        voteRepository.save(vote1);
        voteRepository.save(vote2);
        voteRepository.save(vote3);
    }

}
