## Mysql
### 현재 날짜기준 12개월이 지난 데이터 삭지
```
DELETE 
FROM TB1
WHERE CREATE_DT < DATE_ADD(now(), interval -12 month )

-- 현재시간에 뒤 파라미터를 더한 값 이전으로 삭제한다.
--  SECOND, MINUTE, DAY, YEAR 등 사용이 가능하다
-- -12는 12개월전, 12는 12개월 후 
```
