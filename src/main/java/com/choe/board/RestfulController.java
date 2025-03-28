package com.choe.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        String str = jdbcTemplate.queryForObject("SELECT reserve_date FROM titan.hwang WHERE name = 'son'", String.class);
        return str;
    }

    @GetMapping("/test4")
    public List<Hwang> test4() {
        String sql = "SELECT * FROM titan.hwang";

        List<Hwang> hwangList = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            Hwang obj = new Hwang();
            obj.setId(((Integer) row.get("id")));
            obj.setName((String) row.get("name"));
            obj.setReserveDate(((String) row.get("reserve_date")));
            obj.setRoomNum(((Integer) row.get("room_num")));
            hwangList.add(obj);
        }

        return hwangList;
    }

    @PostMapping("/submit")
    public String submit(@RequestBody Post post, HttpServletRequest request) {
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
        String sql = "INSERT INTO titan.posts(title, author, content, post_date) VALUES ('" + title + "', '" + author + "', '" + content + "', '" + postDate + "')";
        jdbcTemplate.execute(sql);
        return "success";
    }

    @PostMapping("/submitEdit")
    public String edit(@RequestBody Post post) {
        String title = post.getTitle();
        String content = post.getContent();
        int id = post.getId();

        String sql = "UPDATE titan.posts SET is_edited = TRUE, title = '" + title + "', content = '" + content + "' WHERE id = " + id;
        jdbcTemplate.execute(sql);
        return "success";
    }

    @PostMapping("/regiSubmit")
    public String submit2(@RequestBody User user) {
        String sql = "INSERT INTO titan.\"user\"(username, password, phone, email) VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "', '" + user.getPhone() + "', '" + user.getEmail() + "')";
        jdbcTemplate.execute(sql);
        return "success";
    }

    @PostMapping("/checkDuplicate")
    public String submit3(@RequestBody User user) {
        String sql = "SELECT * FROM titan.\"user\" WHERE username = '" + user.getUsername() + "'";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        if (rows.size() >= 1) {
            return "exists";
        }
        return "success";
    }

    @PostMapping("/checkLogin")
    public String submit4(@RequestBody User user) {
        String sql = "SELECT * FROM titan.\"user\" WHERE username = '" + user.getUsername() + "'";
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

    @PostMapping("/delete-post")
    public String deletePost(@RequestBody Post post, HttpServletRequest request) {
        String loginIdCookieValue = WebUtils.getCookie(request, "login_id").getValue();
        if (!loginIdCookieValue.equals(post.getAuthor())) {
            return "failed";
        }
        String sql = "DELETE FROM titan.posts WHERE id = " + post.getId();
        jdbcTemplate.execute(sql);
        return "success";
    }

    @PostMapping("/is-editable")
    public String isEditable(@RequestBody Post post, HttpServletRequest request) {
        String loginIdCookieValue = WebUtils.getCookie(request, "login_id").getValue();
        if (!loginIdCookieValue.equals(post.getAuthor())) {
            return "failed";
        }
        return "success";
    }

    @PostMapping("/submitComment")
    public String createComment(@RequestBody Comment comment, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "login_id");
        String author;
        if (loginCookie != null) {
            author = loginCookie.getValue();
        } else {
            return "needs login";
        }
        String content = comment.getContent();
        Integer postId = comment.getPostId();
        String sql = "INSERT INTO titan.comments (author, content, post_id) VALUES ('" + author + "','" + content + "', " + postId + ")";
        jdbcTemplate.execute(sql);
        return "success";
    }

    @PostMapping("/delete-comment")
    public String deleteComment(@RequestBody Comment comment, HttpServletRequest request) {
        String loginIdCookieValue = WebUtils.getCookie(request, "login_id").getValue();
        if (!loginIdCookieValue.equals(comment.getAuthor())) {
            return "failed";
        }
        String sql = "DELETE FROM titan.comments WHERE id = " + comment.getId();
        jdbcTemplate.execute(sql);
        return "success";
    }
}