package assignment.firstpost.controller;


import assignment.firstpost.dto.PostRequestDto;
import assignment.firstpost.dto.PostResponseDto;
import assignment.firstpost.entity.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final JdbcTemplate jdbcTemplate;

    public PostController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        Post post = new Post(requestDto);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO post(title, name, password, contents) values (?,?,?,?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, post.getTitle());
                    preparedStatement.setString(2, post.getName());
                    preparedStatement.setString(3, post.getPassword());
                    preparedStatement.setString(4, post.getContents());
                    return preparedStatement;
                },
                keyHolder);

        // DB insert 받아온 기본키 확인하기
        String name = post.getName();
        post.setName(name);

        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;

    }

    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        //DB 조회
        String sql = "SELECT * FROM post";

        return jdbcTemplate.query(sql, new RowMapper<PostResponseDto>() {

            @Override
            public PostResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {

                String title = rs.getString("title");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String contents = rs.getString("contents");
                String fromdate = rs.getString("fromdate");
                return new PostResponseDto(title, name, password, contents, fromdate);

            }
        });

    }


    @PutMapping("/posts/{password}")
    public String updatePost(@PathVariable String password, @RequestBody PostRequestDto requestDto) {
        Post post = findById(password);
        if (post != null) {

            String sql = "UPDATE post SET title = ?, name = ?, contents = ? WHERE password = ?";
            jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getName(), requestDto.getContents(), password);
            return post.getContents();
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }

    }


    // 삭제하기
    @DeleteMapping("/posts/{password}")
    public String deletePost(@PathVariable String password){
        Post post = new Post();

        if(findById(password) != null){
            String sql = "DELETE FROM post WHERE password = ?";
            jdbcTemplate.update(sql, password);

            return post.getContents();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }

    }



    private Post findById(String password) {
        String sql = "SELECT * FROM post WHERE password = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()){
                Post post = new Post();
                post.setName(resultSet.getString("name"));
                post.setContents(resultSet.getString("contents"));
                post.setPassword(resultSet.getString("password"));
                return post;
            } else{
                return null;
            }
        }, password);
    }


}
