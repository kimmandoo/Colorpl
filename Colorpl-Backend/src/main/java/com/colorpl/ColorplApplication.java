package com.colorpl;

import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.ArrayList;

@SpringBootApplication
@EnableJpaAuditing
public class ColorplApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColorplApplication.class, args);
	}

}
