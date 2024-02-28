package com.mindera.api.message;

import com.mindera.api.domain.User;
import com.mindera.api.exception.UserDoesNotExistsException;
import com.mindera.api.model.UserDTO;
import com.mindera.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMessageReceiver {

    private final UserRepository userRepository;

    @RabbitListener(queues = "USER_LISTENER")
    public UserDTO receiveRequest(@Payload UserMessage message) {
        String requestData = message.getMessage();
        String base64Credentials = requestData.substring(6).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] strings = credentials.split(":");
        String username = strings[0];
        String password = strings[1];

        User user = userRepository.findByUsernameAndPassword(username, password).orElseThrow(UserDoesNotExistsException::new);
        log.info("User Message reply: ", user.toString());
        return new UserDTO(user);
    }
}
