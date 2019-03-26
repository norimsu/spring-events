package io.github.wotjd243.springevents.user.application;

import io.github.wotjd243.springevents.user.domain.User;
import io.github.wotjd243.springevents.user.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private MessageSender emailSender;
    private MessageSender smsSender;

    public UserService(final UserRepository userRepository, final MessageSender emailSender, final MessageSender smsSender) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.smsSender = smsSender;
    }

    @Transactional
    public void join(final long id, final String name) {
        logger.info("Join Step 1: Beginning");
        final User user = new User(id, name);

        logger.info("Join Step 2: Persistence");
        userRepository.save(user);

        emailSender.sendCongratulation(user.getName()); // Send a mail
        smsSender.sendCongratulation(user.getName()); // Send an SMS

        logger.info("Join Step 5: Completed");
    }

    public List<String> lookup() {
        return userRepository.findAll()
                .stream()
                .map(User::getName)
                .collect(Collectors.toList())
                ;
    }
}