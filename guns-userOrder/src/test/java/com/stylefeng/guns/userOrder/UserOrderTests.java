package com.stylefeng.guns.userOrder;

import com.stylefeng.guns.rest.UserOrderApplication;
import com.stylefeng.guns.rest.common.util.FTPUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserOrderApplication.class)
public class UserOrderTests {

	@Autowired
	private FTPUtil ftpUtil;

	@Test
	public void contextLoads() {
		String fileStrByAddress = ftpUtil.getFileStrByAddress("/MeetingFilm/userOrder/cgs.json");
		System.out.println(fileStrByAddress);
	}

}
