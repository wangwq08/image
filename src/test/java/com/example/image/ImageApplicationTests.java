package com.example.image;

import com.example.image.domain.ImagePath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageApplicationTests {

	@Autowired
		private ImagePath ip;
	@Test
	public void contextLoads() {
		System.out.println(ip.getPath());
        System.out.println(ip.getCpath());
		System.out.println(ip.getTpath());
        System.out.println(ip.getTarPath());
	}

}
