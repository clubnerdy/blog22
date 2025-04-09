package shop.mtcoding.blog.love;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.Resp;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@RestController  // 무조건 데이터만 리턴할 때 사용할 수 있음, 파일을 전달하지 않음
public class LoveController {
    private final LoveService loveService;
    private final HttpSession session;

    @PostMapping("/love")
    public Resp<?> saveLove(@RequestBody LoveRequest.SaveDTO reqDTO){
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("인증이 필요합니다");

        LoveResponse.SaveDTO respDTO = loveService.좋아요(reqDTO, sessionUser.getId());

        return Resp.ok(respDTO);
    }

    @DeleteMapping("/love/{id}")
    public Resp<?> deleteLove(@PathVariable("id") Integer id){
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("인증이 필요합니다");

        LoveResponse.DeleteDTO respDTO = loveService.좋아요취소(id);

        return Resp.ok(respDTO);
    }
}
