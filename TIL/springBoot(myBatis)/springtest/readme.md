## Database 세팅
### hikariCP
데이터 베이스의 Connection을 무한하게 생성하지 못하도록 Pool 형태로 관리하여 과도한 DB 부하를 막아주는 역할 

설정방법
```
package com.springtest.springtest.dataBase;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application.yml")
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig(){
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() throws Exception{
        DataSource dataSource = new HikariDataSource(hikariConfig());
        return dataSource;
    }
}

```

### Mybatis
JDBC의 형태로 사용하게 되면 java소스 내부에 쿼리문을 가지도록 됨
JDBC 예제
```
  public List<NoticeView> getList() throws ClassNotFoundException, SQLException {
      int page = 1;
      List<NoticeView> list = new ArrayList<>();
      int index = 0;
      
      String sql = "SELECT * FROM Notice ORDER BY regdate DESC LIMIT 10 OFFSET ?";    
      String url = "jdbc:mysql://dev.notead.com:0000/address?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
 
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(url, "address", "123");         
      PreparedStatement st = con.prepareStatement(sql);
      st.setInt(1, (page-1)*10); 
      
      ResultSet rs = st.executeQuery();
            
      while (rs.next()) {
         NoticeView noticeView = new NoticeView();
         noticeView.setId(rs.getInt("ID"));
         noticeView.setTitle(rs.getString("TITLE"));
         noticeView.setWriterId(rs.getString("writerId"));
         noticeView.setRegdate(rs.getDate("REGDATE"));
         noticeView.setHit(rs.getInt("HIT"));
         noticeView.setFiles(rs.getString("FILES"));
         noticeView.setPub(rs.getBoolean("PUB"));
         
         list.add(noticeView);      
      }
 
      rs.close();
      st.close();
      con.close();
      
      return list;
   }
출처: https://hyoni-k.tistory.com/70 [Record *:티스토리]
```

mybatis는 SQL문이 java 소스로부터 분리되어 XML로 관리되며 입력 데이터의(조건문과 같은) 바인딩과 출력데이터를 객체로 매핑해준다

maper xml 
```

```

mybatis 설정
```

@Configuration
@PropertySource("classpath:/application.yml")
public class DataSourceConfig {
    ...
    /// 위는 hikari CP 설정 아래는 Mybatis 설정

    @Bean
    public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver  resolver = new PathMatchingResourcePatternResolver(); 
        sessionFactoryBean.setMapperLocations(
            resolver.getResources("classpath:/mapper/sql-*.xml")
        );
        return sessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sessionTemplate( SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

```


### 기타 설정 
JAVA는 Camel Case, DB는 Snake Case를 사용한다 이를 서로 매핑 시켜주기 위해 
applycation.yml에 myBatis 설정을 추가

```
mybatis:
  configuration:
    map-underscore-to-camel-case: true
```

### mapper 생성
mapper를 사용할 interface 
```
@Mapper
public interface BoardMapper {
    List<BoardDto> selectBoardList();
}
```

xmml
namespace= 인터페이스, id 는 method명, resultType은 결과와 매핑한 class 

```
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="board.mapper.BoardMapper">
    <select id="selectBoardList" resultType="com.springtest.springtest.board.BoardDto">
    <![CDATA[
        SELECT 
            board_idx,
            title,
            contents,
            hit_cnt,
            creator_id,
            create_dt
        FROM   
            t_board
        WHERE
            delete_yn = "N"
        ORDER BY board_idx DESC
        

    ]]>]
    </select>

</mapper>
```

### html의 table Col 사이즈 잡는 방법
```

```