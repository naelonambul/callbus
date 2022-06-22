package com.bus.call.interceptor;

import com.bus.call.domain.AccountType;
import com.bus.call.exception.MemberNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //게스트 체크
        if(request.getHeader("Authorization") == null ||
                request.getHeader("Authorization") == " "
        ) {
            request.setAttribute("accountType", "guest");
            return true;
        }

        // 입력값 초기화
        String authorization = request.getHeader("Authorization");
        List<String> typeAndId = Arrays.asList(authorization.split(" "));
        String accountType = typeAndId.get(0).toUpperCase();
        Integer memberId = Integer.parseInt(typeAndId.get(1));

        //변수 검증
        if(typeAndId.size() != 2 || memberId == null || memberId < 0) new MemberNotFound();

        //회원 타입 확인
        Arrays.stream(AccountType.values())
                .map(v->v.name())
                .filter(v -> v.equals(accountType))
                .findAny()
                .orElseThrow(()-> new MemberNotFound());

        //컨트롤러에 값 넘겨줌
        request.setAttribute("member_id", memberId);
        request.setAttribute("accountType", accountType);

        return true;
    }
}
