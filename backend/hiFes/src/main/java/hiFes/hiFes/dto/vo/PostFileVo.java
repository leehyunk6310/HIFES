package hiFes.hiFes.dto.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostFileVo {
    private String postId;
    private String title;
    private String content;
    private List<MultipartFile> files;
}