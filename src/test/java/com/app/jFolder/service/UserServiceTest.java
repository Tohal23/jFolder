package com.app.jFolder.service;

import com.app.jFolder.domain.User;
import com.app.jFolder.repos.UserRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void addUserTest() {
        User user = new User();
        user.setEmail("someEmail@mail.com");
        user.setPassword("");

        boolean isAdded = userService.addUser(user);

        Assert.assertTrue(isAdded);
        Assert.assertNotNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1)).send(ArgumentMatchers.eq(user.getEmail())
                                                                            , ArgumentMatchers.anyString()
                                                                            , ArgumentMatchers.anyString());
    }

    @Test
    public void AddUserFailTest() {
        User user = new User();
        user.setUsername("Admin");

        Mockito.doReturn(new User()).when(userRepo).findByUsername("Admin");

        boolean isAdded = userService.addUser(user);

        Assert.assertFalse(isAdded);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0)).send(ArgumentMatchers.anyString()
                , ArgumentMatchers.anyString()
                , ArgumentMatchers.anyString());
    }

    @Test
    public void ActiveUserTest() {
        User user = new User();
        user.setActivationCode("activeCode");

        Mockito.doReturn(user).when(userRepo).findByActivationCode("activationCode");

        boolean isActivated = userService.activeUser("activationCode");

        Assert.assertTrue(isActivated);
        Assert.assertNull(user.getActivationCode());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void ActiveUserFailTest() {
        boolean isActivated = userService.activeUser("activationCode");

        Assert.assertFalse(isActivated);

        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}
