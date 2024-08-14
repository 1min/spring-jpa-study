package org.example.init.item;

import lombok.RequiredArgsConstructor;
import org.example.init.comment.Comment;
import org.example.init.comment.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final S3Service s3Service;
    private final CommentRepository commentRepository;

    @GetMapping("/list")
    public String list(Model model){
        List<Item> result = itemRepository.findAll();
        model.addAttribute("items", result);

        return "list.html";
    }

    @GetMapping("/write")
    public String write(){
        return "write.html";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item){
        itemService.saveItem(item);

        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Integer id, Model model) {

        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()){
            model.addAttribute("data",result.get());

            List<Comment> comments = commentRepository.findAllByParentId(id);
            model.addAttribute("comments", comments);

            return "detail.html";
        } else {
            return "redirect:/list";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Optional<Item> result = itemRepository.findById(id);
        if(result.isPresent()){
            model.addAttribute("data", result.get());
            return "edit.html";
        }else{
            return "redirect:/list";
        }
    }

    @PostMapping("/edit")
    public String editItem(@ModelAttribute Item item){
        itemService.editItem(item);
        return "redirect:/list";
    }

    @DeleteMapping("/item")
    public ResponseEntity<String> deleteItem(@RequestParam Integer id){
        itemService.deleteItem(id);
        return  ResponseEntity.ok().body("삭제완료");
    }

    @GetMapping("/list/page/{pageNum}")
    String getListPage(Model model, @PathVariable Integer pageNum) {
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(pageNum-1, 5));
        Slice<Item> resultSlice = itemRepository.findSliceBy(PageRequest.of(pageNum-1,5));
        model.addAttribute("items", result);
        System.out.println(result.getTotalPages());
        System.out.println(result.hasNext());
        System.out.println(resultSlice.hasNext());
        return "list.html";
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename){
        String folderPath = "test/";
        var result = s3Service.createPresignedUrl(folderPath + filename);
        return result;
    }

    @PostMapping("/search")
    String postSearch(@RequestParam String searchText, Model model) {
//        itemRepository.findAllByTitleContains(searchText);
         List<Item> searchedItems = itemRepository.fullTextSearch(searchText);
        model.addAttribute("items", searchedItems);
        return "list.html";
    }
}
