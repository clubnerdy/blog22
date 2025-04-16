package shop.mtcoding.blog._core.error;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import shop.mtcoding.blog._core.error.ex.Exception400;

import java.util.List;

//Aspect, PointCut, Advice
@Aspect // 관점 관리
@Component
public class GlobalValidationHandler {

    // 관심사를 분리시킴-> 실제 관심사는 얘가 아니라 핵심로직이니까. 이건 부가로직임.
    // PostMapping 혹은 PutMapping이 붙어있는 메서드를 실행하기 직전에 Advice를 호출하라.
    // 포인트컷↑
    // 내부적으로 문자열 안에 있는걸 파싱함. 크게 의미두지말고 이해함됨.
    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void badRequestHandler(JoinPoint jp) { // jp는 실행될 실제 메서드의 모든 것을 투영하고 있다.
        Object[] args = jp.getArgs(); //메서드의 매개변수들. 배열로 리턴됨.
        for (Object arg : args) { // 매개변수 갯수만큼 반복 (어노테이션은 제외)

            // Errors 타입이 매개변수에 존재하고,
            if (arg instanceof Errors) {
                System.out.println("에러 400 처리 필요함!");
                Errors errors = (Errors) arg;

                // 에러가 존재한다면.
                if (errors.hasErrors()) {
                    List<FieldError> fErrors = errors.getFieldErrors(); // 컬렉션을 리턴함

                    for (FieldError fieldError : fErrors) {
                        throw new Exception400(fieldError.getField() + ":" + fieldError.getDefaultMessage());
                        //필드명 : 에러메세지 로 출력될거임
                    }
                }
            }
        }

    }
}
