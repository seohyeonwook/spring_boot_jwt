package com.git.study.aspect;

import com.git.study.security.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class SecurityAspect { // 토큰인증이 안된건(메소드) 에러를 던진다

    @Autowired
    SecurityService securityService;

    @Before("@annotation(tokenRequired)") // Before = 메소드 호출전에 실행되는 코드
                                            // 호출되기전에 tokenRequired(인터페이스) 이 코드 실행
    // 즉, @TokenRequired가 있는 메서드가 실행되기 전에,
    // 이 메서드(authenticateWithToken)가 먼저 호출됩니다.
    public void authenticateWithToken(TokenRequired tokenRequired) {
        //이 메서드는 실제로 인증을 처리하는 로직입니다.
        // TokenRequired 어노테이션이 붙은 메서드가 호출되면,
        // 이 메서드가 먼저 실행되면서 토큰의 유효성을 검사합니다.
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        // 코드는 현재 요청에 대한 정보를 가져오는 부분입니다.
        // **RequestContextHolder**는 현재 요청의 정보를 관리하는 클래스인데,
        // 여기서 ServletRequestAttributes 객체를 통해 현재 요청에 대한 세부 정보를 얻어옵니다.
        HttpServletRequest request = requestAttributes.getRequest();
        //이 코드는 현재 HTTP 요청을 가져옵니다. HttpServletRequest 객체는 클라이언트의 요청 정보를 담고 있으며,
        // 헤더, 파라미터, URL 등을 포함하고 있습니다.

        String token = request.getHeader("token");
        // token String으로 만듬 // header에 token 넣는다
        //요청의 헤더에서 "token"이라는 이름으로 토큰을 가져옵니다.
        // HTTP 헤더는 클라이언트가 서버로 데이터를 전송할 때 추가적으로
        // 정보를 포함시키는 곳으로, 인증 토큰 같은 정보를 담고 있을 수 있습니다.
        if(StringUtils.isEmpty(token)) { //비어있는지 확인
            throw new IllegalArgumentException("token is empty");
            // 비어있으면 에러 던지기
            // 이 부분은 토큰이 없을 때 발생하는 에러 처리
        }

        if(securityService.getClaims(token) == null || securityService.getSubject(token) == null) {
            //이 조건은 토큰의 유효성을 검사합니다. **securityService.getClaims(token)**는
            // 토큰에서 클레임(claim) 정보를 추출하고, **securityService.getSubject(token)**는
            // 토큰의 주체(subject) 정보를 추출합니다. 이 두 값이 null이면, 토큰이 잘못되었거나
            // 유효하지 않은 상태입니다.
            throw new IllegalArgumentException("token error!! claims or subject are null !!");
        }
        // localhost:9000/users/test1 =이렇게 요청하면 token is empty 뜬다 -> 내가 토큰만료시간 2분으로해둬서
        // http://localhost:9000/security/create/token?subject=test1 이렇게 토큰 다시 만들어주고
        //  localhost:9000/users/test1 여기 headers에 key에 token 넣고 value에 토큰값 넣어주면 조회가 된다

        // subject 기반으로 자체인증로직
    }
    //전체 흐름 정리:

    //1. 메서드 실행 전: @TokenRequired 어노테이션이 있는 메서드가 호출되기 전에
    // 먼저 이 로직이 실행됩니다.

    //2. 요청에서 토큰 추출: HTTP 요청의 헤더에서 "token" 값을 가져옵니다.

    //3. 토큰 검증: 토큰이 비어 있는지 확인하고, 토큰의 클레임과 주체 정보를 검사합니다.

    //4. 에러 처리: 토큰이 없거나 유효하지 않으면 에러를 발생시켜 클라이언트에게 문제를 알립니다.

    //이 과정은 인증을 자동화하여, 특정 메서드를 호출하기 전에 토큰을 검증하고,
    // 잘못된 요청을 미리 차단하는 역할을 합니다.
}
