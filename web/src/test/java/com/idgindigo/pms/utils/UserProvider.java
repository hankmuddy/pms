package com.idgindigo.pms.utils;

import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 29.10.13 15:03
 */
@Component
public class UserProvider extends EntityProvider<User> {
    @Inject
    private UserRepository userRepository;
    @Inject
    private RoleProvider roleProvider;
    @Inject
    private AuthenticationProvider authenticationProvider;
    private long tenantId = 1;


    @Override
    public User createAndFill() {
        User user = new User();
        user.setEmail("SomeEmail " + randomString());
        user.setRole(roleProvider.getPersistentEntity());
        do {
            user.setUsername(randomAlphabeticString());
        } while (userRepository.findByUsername(user.getUsername()) != null);
        user.setPhone(randomAlphabeticString());
        user.setFirstName(randomAlphabeticString());
        user.setLastName(randomAlphabeticString());
        return user;
    }

    @Override
    public User getPersistentEntity(Visitor<User>... visitor) {
        final User user = super.getPersistentEntity(visitor);
        Visitor<Authentication> v = new Visitor<Authentication>() {
            @Override
            public void visit(Authentication entity) {
                entity.setUsername(user.getUsername());
            }
        };
        authenticationProvider.getPersistentEntity(v);
        return user;
    }

    @Override
    public BaseRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    public void updateEntity(User user) {
        user.setEmail("UpdatedEmail " + randomString());
    }

}
