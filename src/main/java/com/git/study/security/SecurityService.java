package com.git.study.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class SecurityService {
    private static final String SECRET_KEY = "sdlkfjsdlkfjldskjglskdjafsdflkdkjfiwnssfdsfjoiqewjv"; // 1. 비밀키 만들기
    // 학습용 원래 이렇게 하면 안됨
    //역할: JWT 토큰을 서명할 때 사용할 **비밀 키(Secret Key)**입니다.
    // 이 키는 서명된 JWT의 유효성을 확인하는 데도 사용됩니다.
    //중요성: 이 키는 보안의 핵심입니다. 따라서 실제 서비스에서는 이렇게 하드코딩하지 않고,
    // 환경변수나 외부 설정 파일에서 관리해야 합니다. 현재는 학습용 코드로,
    // 예시로 사용되고 있습니다.

    // 로그인 서비스 던질때 같이 - 이 클래스에서 사용된 메서드는 2개뿐 구조를 파악할때는 메서드의 구분을 잘 보자
    public String createToken(String subject, long expTime) { // 2. 토큰 구현 메소드
        //역할: 이 메소드는 JWT 토큰을 생성하는 기능을 합니다.
        //subject: 토큰의 주체를 나타내며, 사용자 정보나 이메일과 같은 데이터를
        // 포함할 수 있습니다.
        //expTime: 토큰의 만료 시간을 나타내며, 이 시간이 지나면 토큰이
        // 더 이상 유효하지 않게 됩니다.

        if (expTime <= 0) {
            throw new RuntimeException(" 만료시간이 0 보다 커야함");
        }
        // 역할: 만료 시간이 0 이하인 경우 예외를 발생시키는 간단한 검증 로직입니다.
        // JWT는 만료 시간이 중요한데, 이 조건을 통해 만료 시간을 제대로 설정했는지 확인합니다.

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 3. 서명 알고리즘 -  signatureAlgorithm 에 사용할 알고리즘 저장
        //역할: JWT의 서명 알고리즘을 설정하는 부분입니다. 여기서 사용된 HS256(HMAC SHA-256)은
        // 대칭키 기반의 서명 알고리즘으로, 비밀키 하나만으로 토큰을 서명하고 검증할 수 있습니다.

        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY); // 4. 비밀키 바이트 변환 - 시크릿키는 바이트 단위로 변환해야한다
        //역할: 비밀 키(SECRET_KEY)를 Base64로 인코딩된 문자열로부터 바이트 배열로 변환하는
        // 부분입니다. JWT 서명을 위해서는 키가 바이트 배열 형태여야 합니다.
        // DatatypeConverter 이걸 이용해서 String 형태의 key를 byte로 만들어줌

        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName()); // 5. 서명 키 생성
        //역할: secretKeyBytes와 서명 알고리즘(signatureAlgorithm.getJcaName())을 사용해 서명 Key 객체를 생성합니다.
        // 이 객체는 JWT를 생성할 때 서명에 사용됩니다.
        // SecretKeySpec 암호화하는 키를 만드는것.

        return Jwts.builder() // 5-1. 토큰 생성 - 만든키를 리턴해줘야함
                .setSubject(subject)
                //주어진 주체(subject, 예: 사용자 ID나 이메일)를 / 비밀번호는 시크릿 키를 사용
                // JWT 토큰의 sub 클레임에 설정합니다. 클레임은 토큰에 포함되는 정보로,
                // sub는 토큰의 주체를 나타내는 기본 클레임입니다.

                // - 클레임 - 토큰에 포함된 정보를 나타내며, 이는 주로 인증과 권한 부여에 사용됩니다.
                // 간단히 말해, 클레임은 JWT 내에서 전달되는 데이터로,
                // 사용자와 관련된 정보를 포함할 수 있습니다.
                // -> 클레임 종류는 맨밑에 따로 서술 여기서는subject 사용

                .signWith(signingKey, signatureAlgorithm)
                //JWT 토큰에 서명합니다. 여기서는 앞서 만든
                // signingKey와 HS256 서명 알고리즘(signatureAlgorithm)을 사용해 토큰에 디지털 서명을 추가합니다.

                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                //토큰의 만료 시간을 설정합니다. 현재 시간에서 expTime(밀리초 단위)을
                // 더한 값이 만료 시간으로 설정됩니다

                .compact();
                //모든 설정이 완료되면, JWT 토큰을 문자열 형태로 압축(compact)하여 반환합니다.
                // 이 문자열이 최종적으로 생성된 JWT 토큰입니다

        //정리: 이 부분은 주어진 주체와 만료 시간을 사용해 서명된 JWT 토큰을 생성하고,
        //      그 결과를 반환합니다.
    }

    // 토큰 검증하는 메서드 만들어서 보통 boolean으로 리턴해주지만 여기서는 만들었다
    public String getSubject(String token) { // 6  토큰에서 주체 추출 - 만들었으면 꺼내와서 점검
        Claims claims = Jwts.parserBuilder() // 클레임 정보
                // JWT 파서를 생성하는 빌더 객체입니다.
                // 이 객체는 JWT 토큰을 해석(파싱)할 때 사용됩니다.

                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                // JWT 토큰을 검증할 때 사용할 서명 키를 설정합니다.
                // 이 키는 토큰을 생성할 때 사용한 것과 동일한 **비밀 키(SECRET_KEY)**로,
                // Base64로 인코딩된 비밀 키를 바이트 배열로 변환하여 사용합니다.

                .build()
                //설정이 완료된 파서 객체를 생성합니다.

                .parseClaimsJws(token)
                // 토큰을 넣어서 풀어준다
                //주어진 JWT 토큰을 파싱(해석)합니다. 이 과정에서 서명 검증도 이루어집니다.
                // 만약 서명이 위조되었거나 만료된 토큰이라면 예외가 발생합니다.

                .getBody();
                // 토큰의 클레임(Claims), 즉 토큰에 담긴 데이터를 추출합니다.

        return claims.getSubject(); // subject 꺼내오기
        // 클레임에서 **주체(subject)**를 추출하여 반환합니다.
        // 주체는 createToken 메소드에서 설정한 값입니다.

        //정리: 이 부분은 JWT 토큰을 해석하여 주체를 추출하고, 그 값을 반환합니다.
        // 서명이 올바른지, 토큰이 변조되지 않았는지 확인하는 과정도 함께 진행됩니다.

        // 다 만들었으면 이거 이제 가져다 써야지 controller에서
    }

    // security에서 사용
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}


//위에서 작성한 코드 흐름을 쉽게 설명하면 다음과 같습니다:
//
//1. **JWT 토큰 생성**:
//   - 먼저, **사용자 정보(주체)**와 토큰의 **만료 시간**을 설정합니다.
//   주체는 토큰에 담기는 사용자 정보(예: 사용자 ID)이고,
//   만료 시간은 토큰이 얼마나 오랫동안 유효할지 정합니다.
//   - 만료 시간이 0보다 크지 않으면 오류를 발생시키게 되어, 유효하지 않은 시간을 방지합니다.
//
//2. **암호화 알고리즘 설정**:
//   - JWT 서명을 만들기 위해 사용할 **암호화 알고리즘**을 정합니다.
//   여기서는 **HS256**이라는 알고리즘을 사용합니다.
//   이 알고리즘은 비밀 키로 데이터를 암호화하는 방식입니다.
//
//3. **비밀 키 설정**:
//   - 비밀 키는 **데이터를 안전하게 보호**하기 위한 중요한 값입니다.
//   이 비밀 키를 기반으로 서명을 생성합니다.
//   코드에서는 이 키를 기본적으로 설정해 두었지만,
//   실제 서비스에서는 더 안전한 방식으로 관리되어야 합니다.
//
//4. **서명 키 생성**:
//   - 설정된 비밀 키를 바탕으로 서명에 사용할 **서명 키**를 만듭니다.
//   서명 키는 나중에 데이터를 변조하지 않았는지 확인하는 데 사용됩니다.
//
//5. **JWT 토큰 반환**:
//   - JWT의 구성 요소인 **주체(사용자 정보)**와 **서명 키**를 사용하여 JWT 토큰을 생성하고,
//   여기에 **만료 시간**을 설정합니다. 이렇게 만들어진 토큰은 서버에서 클라이언트에게
//   전달되어 클라이언트는 인증된 상태로 서버와 통신할 수 있습니다.
//
//6. **토큰에서 정보 추출**:
//   - 사용자가 서버에 보낸 JWT 토큰을 분석해, 토큰 안에 들어있는 **사용자 정보(주체)**를
//   꺼냅니다. 이를 통해 서버는 사용자 정보를 확인하고 적절한 처리를 할 수 있습니다.
//
//### 전체적인 흐름:
// 1. 사용자의 정보를 바탕으로 JWT 토큰을 만듭니다.
// 2. 이 토큰에는 사용자 정보와 만료 시간이 담기고, 비밀 키로 서명되어 보호됩니다.
// 3. 클라이언트는 이 토큰을 서버에 전달해 본인이 인증된 사용자임을 증명합니다.
// 4. 서버는 받은 토큰을 해석해 사용자 정보를 확인하고, 토큰이 유효한지 검사합니다.
//
//이 과정은 클라이언트-서버 간 **인증 절차**를 간단하고 안전하게 처리하는 방법으로,
// 주로 웹 애플리케이션의 **로그인**이나 **API 인증**에서 많이 사용됩니다.


//클레임의 종류
//등록된 클레임 (Registered Claims): 이미 정의된 표준 클레임들로,
// 특정 목적을 위해 미리 예약된 정보입니다. 예를 들어:
//
// sub (Subject): 토큰의 주체를 나타냅니다. 보통 사용자 ID나 이메일 같은 사용자 식별 정보가 들어갑니다.
// exp (Expiration): 토큰이 만료되는 시점을 지정합니다.
// iss (Issuer): 토큰을 발급한 주체를 나타냅니다.
// aud (Audience): 토큰이 발급된 대상을 나타냅니다.

// 클레임의 역할
//클레임은 JWT가 어떤 정보를 포함하고 있는지를 나타내며, 이를 통해 인증, 권한 관리, 세션 관리
// 등의 다양한 작업을 처리할 수 있습니다. 클레임에 들어가는 정보는 암호화되지 않기 때문에
// 민감한 정보는 서명된 토큰에서 직접 포함하지 않는 것이 중요합니다.
//
//예시

//{
//  "sub": "1234567890", // 주체 (subject), 보통 사용자 ID
//  "name": "John Doe",  // 공개 클레임, 사용자 이름
//  "admin": true,       // 비공개 클레임, 사용자 권한 정보
//  "exp": 1672531199    // 만료 시간 (epoch 시간)
//}
//정리: 클레임은 JWT에 포함된 데이터로, 사용자에 대한 정보와 토큰의 상태 등을 나타내며,
// 주로 sub 같은 기본 클레임을 통해 사용자 식별 정보를 전달하고,
// exp 같은 정보로 토큰의 유효성을 관리합니다.