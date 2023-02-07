package com.example.dataprocess.config;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.example.dataprocess.DynamicData.DynamicDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Bean
    @Primary
    @DependsOn({"springUtils", "druidDataSource"})
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(DynamicDataSource.dataSourcesMap);
        return dynamicDataSource;
    }

    /**
     * 配置监控服务器
     * @return 返回监控注册的servlet对象
     */
    @Bean 
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 添加白名单
        bean.addInitParameter("allow", "localhost");
        // 添加黑名单,黑名单和白名单重复时黑名单优先级最高
        // bean.addInitParameter("deny", "");

        // 添加控制台管理员用户
        bean.addInitParameter("loginUsername", "admin");
        bean.addInitParameter("loginPassword", "admin");

        // 是否能重置数据
        bean.addInitParameter("resetEnable", "false");

        return bean;
    }
     /**
     * 配置服务过滤器
     * @return 返回过滤器配置对象
     */
    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则
        bean.addUrlPatterns("/*");
        // 忽略过滤格式
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");

        return bean;

    }
}

