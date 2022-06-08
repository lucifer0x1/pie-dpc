import com.pie.dpc.server.InstallAgentExecutor;
import com.pie.dpc.server.status.ConnectSSHServerCheck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author wangxiyue.xy@163.com
 * @Date 2022/5/16 19:01
 * @Description TODO :
 **/
@SpringBootTest(classes ={ConnectSSHServerCheck.class, InstallAgentExecutor.class})
public class UseMainResourcesTestInstallTest {


    @Autowired
    InstallAgentExecutor install;

    @Test
    void install() {

        if (install.getConnect().connect("root","root","127.0.0.1",10022)) {
            System.out.println(install.install("/app"));
        }


    }
}
