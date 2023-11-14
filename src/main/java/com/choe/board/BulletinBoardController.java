package com.choe.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class BulletinBoardController {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @GetMapping("/board")
    public String board(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page) {
        int count = 10;
        int start = (page-1) * count;
        String sql = "select * from titan.posts ORDER BY post_date DESC limit " + start + ", " + count;

        List<Post> postList = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            Post obj = new Post();

            obj.setId(((Integer) row.get("id")));
            obj.setTitle((String) row.get("title"));
            obj.setAuthor((String) row.get("author"));
            // Spring returns BigDecimal, need convert
            obj.setPostDate((Date) row.get("post_date"));
            obj.setContent(((String) row.get("content")));
            postList.add(obj);
        }

        int totalPosts = jdbcTemplate.queryForObject("select count(*) from titan.posts", Integer.class);
        int totalPages = (int) Math.ceil((double) totalPosts / count);


        model.addAttribute("posts", postList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "bulletin";
    }
    @GetMapping("/post")
    public String post() {
        return "createpost";
    }




}
