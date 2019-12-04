package cz.upce.webapp.services;

import cz.upce.webapp.service.SecurityServiceImpl;
import feign.FeignException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Jan Houzvicka
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@Ignore
public class SecurityServiceImplTest {

    @Autowired
    private SecurityServiceImpl securityService;

    @Test
    public void loginUserSuccess() {
        securityService.login("test@test.cz", "test");
    }

    @Test(expected = AuthenticationException.class)
    public void loginUserFailedWhenBadPasswordInserted() {
        securityService.login("test@test.cz", "spatneheslo");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loginUserFailedWhenBadUsernameInserted() {
        securityService.login("", "");
    }

}