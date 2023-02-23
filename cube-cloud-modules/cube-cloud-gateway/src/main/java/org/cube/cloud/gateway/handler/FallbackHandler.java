package org.cube.cloud.gateway.handler;

import org.cube.commons.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * 异常降级处理
 *
 * @author 杨欣武
 * @version 2.4.2
 * @since 2022/05/17
 */
@Slf4j
@Component
public class FallbackHandler implements HandlerFunction<ServerResponse> {

    private static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        Optional<Object> originalUris = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        originalUris.ifPresent(originalUri -> log.error("网关执行请求：{} 失败，服务降级处理！", originalUri));
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromValue(Result.error("访问超时，请稍后再试！")));
    }
}
