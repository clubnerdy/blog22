package shop.mtcoding.blog._core.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.mtcoding.blog._core.error.ex.*;
import shop.mtcoding.blog._core.util.Resp;

@RestControllerAdvice// @ControllerAdvice
public class GlobalExceptionHandler {
    // 모든 에러가 여기로 들어오는데 나눠져야하니까

    // ** 발견되지 않은 모든 Exception **
    // => 로그를 확인해서 유지보수해야함!
    @ExceptionHandler(Exception.class)
    public String exUnKnown(Exception e){
        String html = """
                <script>
                    alert('${msg}');
                    history.back();
                </script>
                """.replace("${msg}", "관리자에게 문의해주세요");

        System.out.println("관리자님 보세요 : "+e.getMessage());

        return html;
    }

    // ** 404 ** 자원을 찾을 수 없음
    @ExceptionHandler(Exception404.class)
    public String ex404(Exception404 e){
        String html = """
                <script>
                    alert('${msg}');
                </script>
                """.replace("${msg}", e.getMessage());

        return html;
    }

    // ** 403 ** 권한 없음
    @ExceptionHandler(Exception403.class)
    public String ex403(Exception403 e){
        String html = """
                <script>
                    alert('${msg}');
                </script>
                """.replace("${msg}", e.getMessage());

        return html;
    }

    // ** 403 ** 권한 없음 ajax 처리
    @ExceptionHandler(ExceptionApi403.class)
    public Resp<?> exApi403(ExceptionApi403 e){

        return Resp.fail(403,e.getMessage());
    }


    // ** 401 ** 인증 안됨
    @ExceptionHandler(Exception401.class)
    public String ex401(Exception401 e){
        String html = """
                <script>
                    alert('${msg}');
                    location.href = "/login-form";
                </script>
                """.replace("${msg}", e.getMessage());

        return html;
    }

    // ** 401 ** 인증 안됨 ajax 처리
    @ExceptionHandler(ExceptionApi401.class)
    public Resp<?> exApi401(ExceptionApi401 e){

        return Resp.fail(401,e.getMessage());
    }

    // ** 400 ** 잘못된 요청
    @ExceptionHandler(Exception400.class)
    public String ex400(Exception400 e){
        String html = """
                <script>
                    alert('${msg}');
                </script>
                """.replace("${msg}", e.getMessage());

        return html;
    }

    // ** ** 400 ** 잘못된 요청 ajax 처리
    @ExceptionHandler(ExceptionApi400.class)
    public Resp<?> exApi400(ExceptionApi400 e){

        return Resp.fail(400,e.getMessage());
    }
}
