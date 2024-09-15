package com.git.study.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
// 언제 적용될거냐 = 런타임
@Target(ElementType.METHOD)
// 타겟은 뭐로할거냐 elemetype에 메소드
public @interface TokenRequired {
    // 이제 이 어노테이션을 이용한 기능을 클래스로만들어준다 - SecurityAspect
}
