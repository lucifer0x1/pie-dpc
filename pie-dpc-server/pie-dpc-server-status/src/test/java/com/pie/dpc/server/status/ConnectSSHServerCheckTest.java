package com.pie.dpc.server.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ConnectSSHServerCheck.class)
class ConnectSSHServerCheckTest {


    private ConnectSSHServerCheck connectSSHServerCheck = new ConnectSSHServerCheck();

    @Test
    void destroy() {
    }

    @Test
    void executeCommand() {
    }

    @Test
    void connect() {

        System.out.println(connectSSHServerCheck.connect("root",
                "lucy", "192.168.56.101", 22));
        System.out.println(connectSSHServerCheck.executeCommand("ifconfig"));

    }
}