package kr.njw.odeseoul.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "accessToken",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@SecurityScheme(
        name = "refreshToken",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "오디서울",
                description = """
                        오디서울 라이브 API 문서입니다.

                        [안내사항]\040\040
                        모든 API의 응답은 다음과 같은 형태입니다.

                        ```
                        {
                          code: number;
                          message: string;
                          result?: any;
                        }
                        ```

                        `result`에 API 별로 정의된 응답 schema가 들어갑니다.

                        자물쇠 아이콘이 달린 API는 호출시 엑세스 토큰(토큰 갱신 API는 리프레시 토큰)이 필요합니다.\040\040
                        Swagger에서는 페이지 우상단의 Authorize 버튼을 누르면 토큰을 입력할 수 있습니다.

                        엑세스 토큰은 `Authorization: Bearer <accessToken>` 형식의 헤더로 전송합니다.\040\040
                        리프레시 토큰은 HttpOnly 세션쿠키로 자동 포함되어 프론트에서 별도 처리가 필요 없지만,
                        만약 수동 지정하고 싶은 경우 `Authorization: Bearer <refreshToken>` 형식의 헤더로 전송합니다. (권장 X)

                        [공통 응답 code]\040\040
                        200 : 요청에 성공하였습니다.\040\040
                        400 : 입력을 확인해주세요.\040\040
                        401 : 인증에 실패했습니다.\040\040
                        403 : 권한이 없습니다.\040\040
                        404 : 대상을 찾을 수 없습니다.\040\040
                        500 : 서버 오류가 발생했습니다.\040\040
                        99999 : 알 수 없는 오류입니다.""",
                version = "v1"
        ),
        servers = {
                @Server(url = "/", description = "Default API Server")
        }
)
@Configuration
public class OpenApiConfig {
}
