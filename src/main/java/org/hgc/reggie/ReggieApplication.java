package org.hgc.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author hgc
 * @version 1.0
 * @date ${DATE} ${TIME}
 */

@Slf4j
@SpringBootApplication
// @ServletComponentScan: 使用@WebServlet、@WebFilter、@WebListener标记的 Servlet、Filter、Listener 就可以自动注册到Servlet容器中
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
    }
}