package com.itmajing.excel.config;


import com.itmajing.excel.core.Generator;
import com.itmajing.excel.core.TemplateFactory;
import com.itmajing.excel.util.SpringContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="itmajing.com">MaJing</a>
 */
@Configuration
@ConditionalOnClass(Generator.class)
public class ExcelAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public SpringContextUtil getSpringContextUtil() {
        return new SpringContextUtil();
    }

    @Bean
    @ConditionalOnMissingBean
    public TemplateFactory getTemplateFactory() {
        return new TemplateFactory();
    }
}
