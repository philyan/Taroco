package cn.taroco.governance;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 服务治理
 *
 * @author liuht
 * 2019/4/28 15:21
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class TarocoGovernanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TarocoGovernanceApplication.class, args);
    }
}
