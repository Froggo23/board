package com.choe.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String board(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "query", required = false) String query,
                        HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "login_id");

        if (loginCookie == null) {
            return "redirect:/login";
        }

        int count = 10;
        int start = (page - 1) * count;
        List<Post> postList = new ArrayList<>();
        String sql;
        int totalPosts;

        if (query != null && !query.isEmpty()) {
            sql = "SELECT * FROM titan.posts WHERE title ILIKE '%" + query + "%' OR content ILIKE '%" + query + "%' ORDER BY id DESC LIMIT " + count + " OFFSET " + start;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            for (Map<String, Object> row : rows) {
                Post post = new Post();
                post.setId((Integer) row.get("id"));
                post.setTitle((String) row.get("title"));
                post.setAuthor((String) row.get("author"));
                post.setPostDate((Date) row.get("post_date"));
                post.setContent((String) row.get("content"));
                post.setIsEdited((Boolean) row.get("is_edited"));
                postList.add(post);
            }

            String countQuery = "SELECT COUNT(*) FROM titan.posts WHERE title ILIKE '%" + query + "%' OR content ILIKE '%" + query + "%'";
            totalPosts = jdbcTemplate.queryForObject(countQuery, Integer.class);

        } else {
            sql = "SELECT * FROM titan.posts ORDER BY id DESC LIMIT " + count + " OFFSET " + start;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            for (Map<String, Object> row : rows) {
                Post post = new Post();
                post.setId((Integer) row.get("id"));
                post.setTitle((String) row.get("title"));
                post.setAuthor((String) row.get("author"));
                post.setPostDate((Date) row.get("post_date"));
                post.setContent((String) row.get("content"));
                post.setIsEdited((Boolean) row.get("is_edited"));
                postList.add(post);
            }

            totalPosts = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM titan.posts", Integer.class);
        }

        int totalPages = (int) Math.ceil((double) totalPosts / count);

        model.addAttribute("postList", postList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("query", query);

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

        String sql = "SELECT * FROM titan.posts WHERE id = " + postId;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Post post = new Post();
        Map<String, Object> row = rows.get(0);
        post.setTitle((String) row.get("title"));
        post.setContent(((String) row.get("content")));
        model.addAttribute("post", post);
        model.addAttribute("postId", postId);

        return "editpost";
    }

    @GetMapping("/view-post/{id}")
    public String view(Model model, @PathVariable("id") int id, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "login_id");
        if (loginCookie == null) {
            return "redirect:/login";
        }

        String sql = "SELECT * FROM titan.posts WHERE id = " + id;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Post post = new Post();
        Map<String, Object> row = rows.get(0);
        post.setTitle((String) row.get("title"));
        post.setContent(((String) row.get("content")));
        model.addAttribute("post", post);
        model.addAttribute("postId", id);

        String sql2 = "SELECT titan.comments.id, titan.comments.author, titan.comments.content, titan.comments.comment_date, titan.comments.is_edited FROM titan.comments JOIN titan.posts ON titan.comments.post_id = titan.posts.id WHERE titan.posts.id = " + id + " ORDER BY titan.comments.id DESC";

        List<Comment> commentList = new ArrayList<>();

        List<Map<String, Object>> rows2 = jdbcTemplate.queryForList(sql2);

        for (Map row2 : rows2) {
            Comment comment = new Comment();
            comment.setAuthor((String) row2.get("author"));
            comment.setId((Integer) row2.get("id"));
            comment.setCommentDate((Date) row2.get("comment_date"));
            comment.setContent(((String) row2.get("content")));
            comment.setEdited((Boolean) row2.get("is_edited")); // Fixed: use row2 instead of row
            commentList.add(comment);
        }

        model.addAttribute("commentList", commentList);
        return "viewpost";
    }
}