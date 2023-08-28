package assignment.firstpost.dto;

import assignment.firstpost.entity.Post;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String name;
    private String contents;
    private String password;
    private String fromDate;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.name = post.getName();
        this.contents = post.getContents();
        this.password = post.getPassword();
        this.fromDate = post.getFromDate();
    }

    public PostResponseDto(String title, String name, String contents, String password, String fromDate) {
        this.title = title;
        this.name = name;
        this.password = password;
        this.contents = contents;
        this.fromDate = fromDate;
    }


}
