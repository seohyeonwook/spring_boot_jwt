package com.git.study.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @GetMapping("/create/token")
    public Map<String, Object> createToken(@RequestParam(value = "subject") String subject) {
        // http://localhost:9000/security/create/token?subject=kang - postman에서 던지는법 - kang이라는 이름으로 token만들기

        // Map 으로 하는이유가 key = String / value =Object = Object 가 제이슨 형태로나와서
        // RequestParam - web에서 ?뒤에 파라미터 던져보려고
        // 원래라면 subject 가id값이 되고 비밀번호로 key값도 같이던져주는 post방식으로 되어야하는데
        // 보여주기식으로 get
        String token = securityService.createToken(subject, (2 * 1000 * 60)); // 토큰 만들기
        Map<String, Object>map = new LinkedHashMap<>();
        // 리턴할 map생성
        map.put("result", token);
        return map;
    }

    @GetMapping("/get/subject")
    public Map<String, Object> getSubject(@RequestParam(value = "token") String token) {
        // 위에서 get 던지면 result에
        //"result": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrYW5nIiwiZXhwIjoxNzI2MzY5ODAyfQ.3LrB-ehwUDEckOMRTjKX1ofDLZvTKTZrfcMDCM-DFPo"
        // 이런 token값이 생성되는데 이걸 postman에서
        // http://localhost:9000/security/get/subject?token=
        // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrYW5nIiwiZXhwIjoxNzI2MzcwMjI4fQ.vnywd9I-r1yx-jqtIZbm6f-N6qIGXFN9ey0m68Qje2Y
        // 이렇게 토큰 유효시간내에 다시 날려보면 kang을 보여준다

        String subject = securityService.getSubject(token); // 토큰을 넣어서 subject만듬
        Map<String, Object>map = new LinkedHashMap<>(); // 토큰리턴이아니라 서브젝트 리턴해주기위해서
        map.put("result", subject);
        return map;

        // key 값은 점으로 구분된다
        // 헤더 - eyJhbGciOiJIUzI1NiJ9. -
        // 페이로드 - eyJzdWIiOiJrYW5nIiwiZXhwIjoxNzI2MzcwMjI4fQ. - 내용 / 정보들이(서브젝트 만료시간 등등) 만들어지는것 -클레임
        // 서명값 - vnywd9I-r1yx-jqtIZbm6f-N6qIGXFN9ey0m68Qje2Y - 인코딩한 헤더/ 페이로드가 key로 해싱시킨 서명값이 들어간다
        // jwt.io 사이트에 들어가서 인코드에 암호화한 키값 넣어보면 검증이가능하다
    }
}
