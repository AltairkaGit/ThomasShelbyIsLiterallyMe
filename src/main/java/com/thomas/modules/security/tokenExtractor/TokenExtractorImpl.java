package com.thomas.modules.security.tokenExtractor;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TokenExtractorImpl implements TokenExtractor {
    private List<ExtractTokenStrategy> strategies;
    private final ListableBeanFactory beanFactory;

    @Autowired
    public TokenExtractorImpl(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    private void SetStrategies() {
        Map<String, ExtractTokenStrategy> extractTokenStrategyBeans = beanFactory.getBeansOfType(ExtractTokenStrategy.class);
        strategies = new ArrayList<>(extractTokenStrategyBeans.values());

    }
    @Override
    public String extract(HttpServletRequest req) {
        for (ExtractTokenStrategy s : strategies) {
            String token = s.extractToken(req);
            if (token != null) return token;
        }
        return null;
    }

    @Override
    public String extract(ServerHttpRequest req) {
        HttpServletRequest httpServletRequest = null;
        if (req instanceof ServletServerHttpRequest) {
            httpServletRequest = ((ServletServerHttpRequest) req).getServletRequest();
        } else if (req instanceof NativeWebRequest) {
            NativeWebRequest nativeWebRequest = (NativeWebRequest) req;
            httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        }

        if (httpServletRequest != null)
            return extract(httpServletRequest);
        return null;
    }
}
