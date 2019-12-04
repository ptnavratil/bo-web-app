package cz.upce.webapp.service;

import cz.upce.webapp.dao.users.model.User;
import cz.upce.webapp.dao.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;

/**
 * @author Jan Houžvička
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        Optional<User> userNullable = userRepository.findByEmail(email);
        if(!userNullable.isPresent())
        {
            throw new UsernameNotFoundException("User was not found");
        }

        User user = userNullable.get();
        org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new HashSet<>());
        return user1;
    }
}
