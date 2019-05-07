package cn.taroco.oauth2.client1.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 Controller
 *
 * @author liuht
 * 2019/5/7 11:01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @GetMapping
    public Object user(OAuth2Authentication authentication) {
        return authentication.getUserAuthentication();
    }

    @GetMapping("/remote")
    public Object remote() {
        System.out.println(restTemplate.getAccessToken().getValue());
        final ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:9005/resource/user", String.class);
        return entity.getBody();
    }
}
