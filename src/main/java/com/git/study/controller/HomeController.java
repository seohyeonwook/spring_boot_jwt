package com.git.study.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String test() {
        return "test!!";
    }

    //Oauth 는 jwt 토큰 기반으로 구현 가능하다

    // 토큰 - 클라이언트에서 로그인 요청하면 서버에서 토큰을 생성해서 다시 클라이언트로
    //        응답을 해주는데 토큰과 같이준다 -> 클라이언트에서 토큰을 들고 있는다
    //        그 후 예를들어 클라이언트에서 다른 서비스요청을 서버로 던지면 요청을하면서
    //        토큰을 같이 보낸다 그럼 서버에서 토큰 검증을하고 맞으면 리턴해준다
    //        원래 항상 정보는 서버에서 들고있었는데 토큰을 사용하면 클라이언트에서 임시로 가지고있다
    //        서버로 요청하고 다시 응답하고 대신 토큰 만료시간을 둔다
    //      서버가 감당하는 부담을 줄인다
}
