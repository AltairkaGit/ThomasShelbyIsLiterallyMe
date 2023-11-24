package com.thomas.config;

import com.thomas.modules.security.composer.FilterComposer;
import com.thomas.modules.security.composer.FilterSequence;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@Order(1)
public class FilterComposerConfig {
    private final ListableBeanFactory beanFactory;

    @Autowired
    public FilterComposerConfig(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    public FilterComposer filterComposer() {
        Map<String, FilterSequence> filterBeans = beanFactory.getBeansOfType(FilterSequence.class);
        List<FilterSequence> filters = new ArrayList<>(filterBeans.values());
        // Sort filters based on @Order annotation
        filters.sort(AnnotationAwareOrderComparator.INSTANCE);
        return new FilterComposer(filters, SecurityConfig.permittedList);
    }


}
