package spring.conf;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:spring/db.properties")
@EnableTransactionManagement
@MapperScan("user.dao")
public class SpringConfiguration {
	@Value("${jdbc.driver}")
	private String driver;
	@Value("${jdbc.url}")
	private String url;
	private @Value("${jdbc.username}") String username;
	private @Value("${jdbc.password}") String password;
	
	private @Value("spring/mybatis-config.xml") String configLocation;
	private @Value("user/dao/userMapper.xml") String mapperLocations;
	
	/* 1번방식
	@Bean(name="dataSource")
	public BasicDataSource dataSource(){
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		basicDataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
		basicDataSource.setUsername("c##java");
		basicDataSource.setPassword("1234");
		return basicDataSource;
	}*/
	@Bean(name="dataSource")
	public BasicDataSource dataSource(){ //2번방식
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(driver);
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);
		
		return basicDataSource;
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext,
											   DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);//데이터 소스를 가지고 있는 메소드호출
		sqlSessionFactory.setTypeAliasesPackage("user.bean");
		sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/*Mapper.xml"));
		
		return sqlSessionFactory.getObject();
	}
	
	@Bean
	 public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) throws Exception {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSessionTemplate;
	  }
	
	@Bean(name="transactionManager")
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	/*
	 @Bean
	 public UserDAO userDAOMybatis() {
	    return new UserDAOMybatis();
	 }*/
}
