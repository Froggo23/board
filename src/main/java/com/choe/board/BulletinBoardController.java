package com.choe.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class BulletinBoardController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/board")
    public String board(Model model, @RequestParam(name = "page", defaultValue = "1") int page, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "login_id");

        if (loginCookie == null) {
            return "redirect:/login";
        }
        int count = 10;
        int start = (page - 1) * count;
        String sql = "select * from titan.posts ORDER BY post_date DESC limit " + start + ", " + count;

        List<Post> postList = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            Post post = new Post();

            post.setId(((Integer) row.get("id")));
            post.setTitle((String) row.get("title"));
            post.setAuthor((String) row.get("author"));
            // Spring returns BigDecimal, need convert
            post.setPostDate((Date) row.get("post_date"));
            post.setContent(((String) row.get("content")));
            postList.add(post);
        }

        int totalPosts = jdbcTemplate.queryForObject("select count(*) from titan.posts", Integer.class);
        int totalPages = (int) Math.ceil((double) totalPosts / count);


        model.addAttribute("postList", postList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "bulletin";
    }

    @GetMapping("/post")
    public String post(HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "login_id");
        if (loginCookie == null) {
            return "redirect:/login";
        }

        return "createpost";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam int postId, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "login_id");
        if (loginCookie == null) {
            return "redirect:/login";
        }

        String edittitle = "";
        String editcontent = "";

        return "editpost";
    }


}
