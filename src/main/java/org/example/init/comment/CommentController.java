package org.example.init.comment;

import lombok.RequiredArgsConstructor;
import org.example.init.member.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    String postComment(@RequestParam String content, @RequestParam Integer parentId, Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        Comment data = new Comment();
        data.setParentId(parentId);
        data.setContent(content);
        data.setUsername(user.getUsername());
        commentService.saveComment(data);
        return "redirect:/detail/" + parentId;
    }
}
