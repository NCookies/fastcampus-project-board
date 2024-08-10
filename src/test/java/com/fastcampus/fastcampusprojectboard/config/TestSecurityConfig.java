package com.fastcampus.fastcampusprojectboard.config;

import com.fastcampus.fastcampusprojectboard.domain.UserAccount;
import com.fastcampus.fastcampusprojectboard.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/*
 * - 이 클래스를 컨트롤러 테스트 클래스에서 import 하면 관련된 테스트 기능을 일괄적으로 추가할 수 있다.
 * - 이렇게 구현하면 컨트롤러 테스트 클래스에서 UserAccountRepository를 MockBean으로 사용할 수 없으니
 *   테스트 코드에서는 표현되지 않는다는 단점이 있다.
 * - 이 경우에는 컨트롤러 테스트에서는 UserAccountRepository를 사용하지 않기 때문에 크게 상관이 없다.
 *   UserAccountRepository는 SecurityConfig의 UserDetailsService에서 호출했기 때문이다.
 * - 만약 WebMvcTest에서 repository 레이어에 대한 빈을 읽었다면 에러가 발생했을 것이다.
 */
@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;

    // 스프링 프레임워크와 관련된 각 테스트 메소드가 실행되기 직전에 이 메소드를 실행한다.
    @BeforeTestMethod
    public void securitySetUp() {
        // UserAccountRepository를 통해서 findById()를 호출하면 옵셔널로 포장된
        // UserAccount 도메인 정보를 반환한다는 mocking 코드
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
                "unoTest",
                "pw",
                "uno-test@email.com",
                "uno-test",
                "test memo"
        )));
    }
}
