package com.choe.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
//		BulletinBoardController bulletinBoardController = new BulletinBoardController();
//		JdbcTemplate jdbcTemplate = new JdbcTemplate();
//		bulletinBoardController.jdbcTemplate = jdbcTemplate;

		SpringApplication.run(BoardApplication.class, args);
	}

}
