package info.bluefoot.winter.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@ComponentScan(basePackages = "info.bluefoot.winter", excludeFilters = { @Filter(Configuration.class) })
public class ApplicationContext {
    
    @Bean(destroyMethod="close", autowire=Autowire.BY_TYPE)
    public DataSource dataSource() throws Throwable {
        com.mchange.v2.c3p0.ComboPooledDataSource ds = new com.mchange.v2.c3p0.ComboPooledDataSource();
        ds.setDriverClass("org.postgresql.Driver");
        ds.setJdbcUrl("jdbc:postgresql://127.8.229.130:5432/winter?user=adminmqswr7r&password=aVIYl1CYY_rE"); //prod: 127.8.229.130 // dev: 127.0.0.1
        ds.setMaxPoolSize(40);
        ds.setMinPoolSize(1);
        ds.setMaxIdleTime(200);
        return ds;
    }
    
    @Bean
    @Inject
    public UserDetailsService userDetailsService(DataSource dataSource) {
        org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl uds = new org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl();
        uds.setDataSource(dataSource);
        return uds;
    }
}
