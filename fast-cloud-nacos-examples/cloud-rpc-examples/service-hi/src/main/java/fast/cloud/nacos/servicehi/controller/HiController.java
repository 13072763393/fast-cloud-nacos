package fast.cloud.nacos.servicehi.controller;

import com.alibaba.fastjson.JSON;
import com.batman.common.swagger.SwaggerConfig;
import fast.cloud.nacos.feign.openapi.SchedualServiceHi;
import fast.cloud.nacos.servicehi.test.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Batman.qin
 * @create 2019-01-23 18:16
 */
@RestController
@RefreshScope
public class HiController implements SchedualServiceHi {
    @Value(value = "${test.name:}")
    private String testName;

    @Value(value = "${common.version:}")
    private String version;

    @Autowired
    private Test test;

    @Autowired
    private SwaggerConfig swaggerConfig;

    @Override
    public String sayHiFromClientOne(String name) {
        System.out.println(testName);
        System.out.println(version);
        System.out.println(test.getName());
        return "hi " + name + " ,i am from port:" + name + JSON.toJSONString(swaggerConfig);
    }
}
