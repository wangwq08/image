package com.example.image;

import com.example.image.domain.ImagePath;
import com.example.image.domain.SqlInfo;
import com.example.image.service.ImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageApplicationTests {

	@Autowired
	private ImageService imageService;

	@Test
	public void contextLoads() {
		System.out.println("数据库连接成功");
		Date date = new Date();//获得系统时间.
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		Timestamp goodsC_date = Timestamp.valueOf(nowTime);
		this.imageService.create(0,"hjfjggjghjg",goodsC_date,0,null);
	}

}
