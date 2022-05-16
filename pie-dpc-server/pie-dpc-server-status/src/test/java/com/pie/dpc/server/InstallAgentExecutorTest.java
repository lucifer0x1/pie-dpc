package com.pie.dpc.server;

import com.pie.dpc.server.status.ConnectSSHServerCheck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes ={ConnectSSHServerCheck.class,InstallAgentExecutor.class})
class InstallAgentExecutorTest {

    @Autowired
    InstallAgentExecutor install;

    @Test
    void install() {

        if (install.connect.connect("root","lucy","192.168.56.101",22)) {
            System.out.println(install.install("/app"));
        }


    }
}