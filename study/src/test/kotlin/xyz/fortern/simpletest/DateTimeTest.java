package xyz.fortern.simpletest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeTest {
	
	@Test
	public void teat1() {
		//LocalDateTime 本身没有时区概念，
		//获取当前时间时，等于当前UTC时间 + 服务时区偏移
		var now = LocalDateTime.now();
		System.out.println(now);//桌面右下角时钟显示的时间
		System.out.println(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());//显示时间戳
		
		System.out.println("=========");
		
		//可以创建带有时区的时间
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		System.out.println(zonedDateTime);
		System.out.println(zonedDateTime.toInstant().toEpochMilli());
	}
}
