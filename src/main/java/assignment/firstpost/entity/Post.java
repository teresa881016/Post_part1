package assignment.firstpost.entity;

import assignment.firstpost.dto.PostRequestDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor

public class Post {
    private Long id;
    private String title;
    private String contents;
    private String name;
    private String password;
    private String fromDate;
    private String toDate;

    public Post(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
    }
    public void update(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.name = requestDto.getName();

    }




}
