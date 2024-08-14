package org.example.init.sales;

import lombok.RequiredArgsConstructor;
import org.example.init.member.CustomUser;
import org.example.init.member.Member;
import org.example.init.member.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SalesController {
    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    String postOrder(String title,
                     Integer price,
                     Integer count,
                     Authentication auth) {

        Sales sales = new Sales();
        sales.setItemName(title);
        sales.setPrice(price);
        sales.setCount(count);
        Member member = new Member();
        member.setId(((CustomUser)auth.getPrincipal()).getId());
        sales.setMember(member);
        salesRepository.save(sales);
        return "list.html";

    }

    @GetMapping("/order/all")
    String getOrder(Authentication auth) {
//        var result = salesRepository.customFindAll();
//          var result = salesRepository.findAll(); // 성능구린대신 join 안써도 들고오긴함
//        var salesDto = new SalesDto();
//        salesDto.itemName = result.get(0).getItemName();
//        salesDto.price = result.get(0).getPrice();
//        salesDto.username = result.get(0).getMember().getUsername();
//        salesDto.count = result.get(0).getCount();

//        System.out.println(result.get(0));
//        System.out.println(result.get(1));
        var result2 = memberRepository.findById(((CustomUser)auth.getPrincipal()).getId());
        System.out.println("테스트");
        System.out.println(result2.get().getSales());


        return "sales.html";
    }
}

class SalesDto {
    public String itemName;
    public Integer price;
    public String username;
    public Integer count;
}
