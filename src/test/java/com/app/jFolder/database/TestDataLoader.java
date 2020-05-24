package com.app.jFolder.database;

import com.app.jFolder.JFolderApplication;
import com.app.jFolder.config.MailConfig;
import com.app.jFolder.config.MvcConfig;
import com.app.jFolder.config.WebSecurityConfig;
import com.app.jFolder.dataseeder.DataSeeder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {JFolderApplication.class,  WebSecurityConfig.class, MvcConfig.class})
public class TestDataLoader {

    @Autowired
    private DataSeeder dataSeeder;

    @WithMockUser(value = "spring")
    @Test
    public void loadData() {
        dataSeeder.initAllDataBase();
    }

}
