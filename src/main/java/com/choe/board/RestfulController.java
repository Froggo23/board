package com.choe.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class RestfulController {

    int sum;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/test")
    public String test() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            list.add(i);
            if (i % 10000 == 0) {
                System.out.println(list.size());
            }
        }
        return "home";
    }

    @GetMapping("/test2")
    public Map<String, Integer> test2(@RequestParam Integer number) {
        Map<String, Integer> map = new HashMap<>();
        map.put("frog", number);
        map.put("borg", number * 3);
        map.put("corg", number + 2);
        sum += number;
        System.out.println(sum);
        return map;
    }


    @GetMapping("/test3")
    public String test3() {
        String str = jdbcTemplate.queryForObject("select reserve_date from titan.hwang where name = 'son'", String.class);
        return str;
    }


    @GetMapping("/test4")
    public List<Hwang> test4() {
        String sql = "select * from titan.hwang";

        List<Hwang> hwangList = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            Hwang obj = new Hwang();

            obj.setId(((Integer) row.get("id")));
            obj.setName((String) row.get("name"));
            // Spring returns BigDecimal, need convert
            obj.setReserveDate(((String) row.get("reserve_date")));
            obj.setRoomNum(((Integer) row.get("room_num")));
            hwangList.add(obj);
        }

        return hwangList;
    }


    @PostMapping("/submit")
    public String submit(@RequestBody Post post, HttpServletRequest request) {
//        String author = post.getAuthor();
        Cookie loginCookie = WebUtils.getCookie(request, "login_id");
        String author;
        if (loginCookie != null) {
            author = loginCookie.getValue();
        } else {
            return "needs login";
        }
        String title = post.getTitle();
        String content = post.getContent();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String postDate = today.format(formatter);
        String sql = "insert into titan.posts(title, author, content, post_date) values ('" + title + "', '" + author + "', ' " + content + "', '" + postDate + "')";
        jdbcTemplate.execute(sql);
        return "success";
    }

    @PostMapping("/regiSubmit")
    public String submit2(@RequestBody User user) {
        String sql = "insert into titan.user(username, password, phone, email) values ('" + user.getUsername() + "', '" + user.getPassword() + "', ' " + user.getPhone() + "', '" + user.getEmail() + "')";
        jdbcTemplate.execute(sql);
        return "success";
    }

    @PostMapping("/checkDuplicate")
    public String submit3(@RequestBody User user) {
        String sql = "select * from titan.user where username = '" + user.getUsername() + "'";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        if (rows.size() >= 1) {
            return "exists";
        }

        return "success";
    }

    @PostMapping("/checkLogin")
    public String submit4(@RequestBody User user) {
        String sql = "select * from titan.user where username = '" + user.getUsername() + "'";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        if (rows.size() == 0) {
            return "error";
        }
        User userRes = new User();
        userRes.setUsername((String) rows.get(0).get("username"));
        userRes.setPassword((String) rows.get(0).get("password"));
        if (!Objects.equals(userRes.getPassword(), user.getPassword())) {
            return "error";
        }

        return "success";
    }


}
