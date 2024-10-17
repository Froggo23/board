package com.choe.board;

import lombok.Data;

import java.util.Date;
@Data
public class Post {

    int id;
    String title;
    String author;
    Date postDate;
    String content;

    Boolean isEdited;

}
