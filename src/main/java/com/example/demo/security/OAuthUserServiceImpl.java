package com.example.demo.security;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    public OAuthUserServiceImpl(){
        super();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);
            try {
                log.info("OAuth user : {}", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            final String username = (String) oAuth2User.getAttributes().get("login");
            final String authProvider = userRequest.getClientRegistration().getClientName();

            UserEntity userEntity = null;

            if(!userRepository.existsByUsername(username)){
                userEntity = UserEntity.builder()
                        .username(username)
                        .authProvider(authProvider)
                        .build();
                userRepository.save(userEntity);
            } else {
                userEntity = userRepository.findByUsername(username);
            }
            log.info("성공 username {} , authProvider {} ", username, authProvider);
        return new ApplicationOAuth2User(userEntity.getId(), oAuth2User.getAttributes());
    }
}
