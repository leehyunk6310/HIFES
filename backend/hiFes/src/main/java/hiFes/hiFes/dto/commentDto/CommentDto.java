package hiFes.hiFes.dto.commentDto;

import hiFes.hiFes.domain.Comment;
import hiFes.hiFes.domain.Post;
import hiFes.hiFes.exception.NotSamePostException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentDto {
    private Long id;
    private String content;
    private Long parentId;
    private Long createdBy;
    private LocalDateTime createdAt;
    private List<CommentDto> childComments;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.createdBy = comment.getCreatedBy();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();

        if (comment.getParent() != null) {
            Post parentPost = comment.getParent().getPost();
            Long parentPostId = parentPost.getId();
            if (!parentPostId.equals(comment.getPost().getId())) {
                throw new NotSamePostException("Post of Parent and Child is Not Same");
            }

            this.parentId = comment.getParent().getId();
        }

        this.childComments = comment.getChild().stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }

}