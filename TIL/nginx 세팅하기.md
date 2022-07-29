
## 개인키 및 인증서 요청서 생성 
1 $ cd /etc/nginx
2 $ openssl genrsa -des3 -out server.key 2048
	- 암호 입력
	3 $ openssl req -new -key server.key -out server.csr
	- 지역정보, 메일정보 등 입력 
	```
		Country Name (2 letter code) [XX]:​KR
		

		State or Province Name (full name) []:​Seoul
		
		Locality Name (eg, city) [Default City]:​Ganseo
		
		Organization Name (eg, company) [Default Company Ltd]:​lgcns
		
		Organizational Unit Name (eg, section) []:​lgcns
		
		Common Name (eg, your name or your server's hostname) []:dejaylgcnscom
		
		Email Address []:mangi25@cnspartner.com
​		
​		
		Please enter the following 'extra' attributes
		
		to be sent with your certificate request
		
		A challenge password []:
		
		An optional company name []:
		
		* 그냥 패스 enter, enter
	
	```

## 개인키 패스워드 제거 (테스트를 원할하기 위해)
1. $ cp server.key server.key.origin
2. $ openssl rsa -in server.key.origin -out server.key
	- Enter pass phrase for server.key.origin: 패스워드 입력
	
## 인증서 생성
1. $ openssl x509 -req -days 3650 -in server.csr -signkey server.key -out server.crt


## nginx 설정파일 
1. /etc/nginx/conf.d/default.conf 파일 수정

주요설정 부분.
```
server {
    listen       443;

	ssl_certificate server.crt;
    ssl_certificate_key server.key;
	ssl				on;
	
	...
	
	location / {
        root   /var/www/appname/dist;
        index  index.html index.htm;
		#웹소켓이 동작하지 않는다면 추가
		proxy_http_version 1.1;
		proxy_set_header Upgrade $http_upgrade;
		proxy_set_header Connection "upgrade";	


	}
	
```

## 반영확인

1. https://아이피 접속 후 비공개 설정 화면 -> 고급 -> 안전하지 않음으로 이동 (공인인증이 아닌 사설인증이기 때문에 발생)
2. 접속 후 url 좌측의 "주의 요함" 클릭 -> 인증서 올바르지 않음 클릭
3. 인증서 확인 가능

------
#  ERROR: upstream sent too big header while reading response header from upstream

## 증상
운영 SSO, 접근 후 개발 SSO 접근 시 쿠키 갱신 API호출 시 404 Not Found Error를 반환함.
개발 SSO만 접근했을 때는 문제가 없었음

## ERROR 확인
nginx 로그 확인 결과 
upstream sent too big header while reading response header from upstream 발생

## 원인 및 해결
 원인: 두개의 SSO를 사용하며 많은 쿠키를 발행 받아 Header가 너무 커져서 문제가 생김
 해결: Nginx의 proxy_buffer_size를 128k로 수정
 ```
 server {
  listen        80;
  server_name   host.tld;

  location / {
    proxy_pass       http://upstream;
    ...

    proxy_buffer_size          128k;
    proxy_buffers              4 256k;
    proxy_busy_buffers_size    256k;
  }
}
 ```
참고: https://ma.ttias.be/nginx-proxy-upstream-sent-big-header-reading-response-header-upstream/

