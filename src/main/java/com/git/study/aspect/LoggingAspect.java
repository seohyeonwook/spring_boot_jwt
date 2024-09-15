package com.git.study.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect // aop aspect 설명 맨 아래
@Component // Aspect 클래스가 어플리케이션이 서버에 올라갈떄같이 로딩됨
public class LoggingAspect {
    // 로깅(logging)은 애플리케이션이 실행되는 동안 발생하는 다양한 정보를 기록하는 과정입니다.
    // 이 기록된 정보를 **로그(log)**라고 하며, 주로 프로그램의 동작 상태, 에러 발생, 성능 문제 등을
    // 추적하기 위해 사용됩니다.

    @Before("execution( * com.git.study.service.*.get*(..))")
    public void loggerBefore() {
        System.out.println("get 으로 시작되는 메서드 시작");
    }

    @After("execution( * com.git.study.service.*.get*(..))")
    public void loggerAfter() {
        System.out.println("get 으로 시작되는 메서드 끝");
    }

    @Around("execution( * com.git.study.controller.UserController.*(..))")
    public Object loggerAround(ProceedingJoinPoint pjp) throws Throwable {
        long beforeTimeMillis = System.currentTimeMillis();
        System.out.println("[Usercontroller] 실행 시작 : "
            +pjp.getSignature().getDeclaringTypeName() + "."
            +pjp.getSignature().getName());
        Object result = pjp.proceed(); //  proceed 기점으로 전 후가 나뉨

        long afterTimeMillis = System.currentTimeMillis() - beforeTimeMillis;
        System.out.println("[Usercontroller] 실행 완료" + afterTimeMillis + "밀리초 소요"
            +pjp.getSignature().getDeclaringTypeName() + "."
            +pjp.getSignature().getName());

        return result;
    }

}
//AOP (Aspect-Oriented Programming)
//AOP는 객체 지향 프로그래밍의 한계를 보완하기 위한 프로그래밍 패러다임입니다.
// 주로 다음과 같은 목적을 가지고 사용됩니다:
//
//관심사의 분리: 애플리케이션의 핵심 로직과 공통적으로 필요한 기능(예: 로깅, 트랜잭션 관리 등)을
// 분리하여 코드의 재사용성과 유지보수성을 높입니다.

//코드 중복 제거: 공통 기능을 여러 클래스에서 중복해서 구현하는 대신,
// 한 곳에서 관리할 수 있습니다.

//Aspect
//Aspect는 AOP의 핵심 개념 중 하나입니다. Aspect는 공통적으로 적용할 관심사를 정의하는 모듈입니다. 예를 들어, 로깅, 보안, 트랜잭션 관리 등이 Aspect가 될 수 있습니다.
//
//AOP와 Aspect의 관계
//Aspect는 AOP의 개념 중 하나로, 공통적으로 적용될 기능(관심사)을 모듈화한 것입니다.
//AOP는 이러한 Aspect를 정의하고 적용하여, 프로그램의 특정 지점(조인 포인트)에서 공통적인 작업을 수행할 수 있도록 하는 프로그래밍 방식입니다.
//즉, AOP는 Aspect를 활용하여 관심사의 분리를 실현하는 프로그래밍 패러다임입니다. Aspect는 AOP의 실제 구현을 구성하는 요소입니다.
