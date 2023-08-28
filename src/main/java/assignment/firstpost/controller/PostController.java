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
        String sql = "INSERT INTO post(title, contents, name, password) values (?,?,?,?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, post.getTitle());
                    preparedStatement.setString(2, post.getContents());
                    preparedStatement.setString(3, post.getName());
                    preparedStatement.setString(4, post.getPassword());

                    return preparedStatement;
                },
                keyHolder);

        // DB insert 받아온 기본키 확인하기
        Long id = keyHolder.getKey().longValue();
        post.setId(id);

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
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String contents = rs.getString("contents");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String fromDate = rs.getString("fromDate");
                String toDate = rs.getString("toDate");
                return new PostResponseDto(id, title, contents, name, password, fromDate, toDate);

            }
        });

    }


    @PutMapping("/posts/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        Post post = findById(id);
        String password = post.getPassword();
        if (post != null) {

            String sql = "UPDATE post SET title = ?, name = ?, contents = ? WHERE id = ? and password = ?";
            jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getName(), requestDto.getContents(), id, password);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }

    }


    // 삭제하기
    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable Long id) {
        Post post = findById(id);
        String password = post.getPassword();

        if (post != null) {
            String sql = "DELETE FROM post WHERE id = ? and password = ?";
            jdbcTemplate.update(sql, id, password);

            return post.getContents();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }

    }


    private Post findById(Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Post post = new Post();
                post.setName(resultSet.getString("name"));
                post.setContents(resultSet.getString("contents"));
                post.setPassword(resultSet.getString("password"));
                return post;
            } else {
                return null;
            }
        }, id);
    }


}
