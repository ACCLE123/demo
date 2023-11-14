package com.example.demo;


import com.example.demo.service.UserService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class DemoApplicationTests {
    @Rule
    public ContiPerfRule i = new ContiPerfRule();
    @Autowired
    public UserService userService;

    @Test
    @PerfTest(invocations = 9000, threads = 1000)
    @Required(max = 1200, average = 250, totalTime = 60000)
    public void func() {
        System.out.println(userService.getUserById(1));
    }
}
