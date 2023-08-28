package assignment.firstpost.dto;

import assignment.firstpost.entity.Post;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String name;
    private String password;
    private String fromDate;
    private String toDate;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.name = post.getName();
        this.password = post.getPassword();
        this.fromDate = post.getFromDate();
        this.toDate = post.getToDate();
    }

    public PostResponseDto(Long id, String title, String contents, String name,String password, String fromDate, String toDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.password = password;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }


}
