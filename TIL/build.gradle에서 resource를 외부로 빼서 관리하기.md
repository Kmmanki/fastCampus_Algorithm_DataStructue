# build.gradle  설정

```
# task는 하나의 function
task allresources {

#doFirst도 있다. doFirst 실행 -> doLast 실행 
	doLast {
		#MSA구조에서 모든 프로젝트들을 순회하는 이터레이터 역할
		allprojects.each { project ->
			copy {
				#프로젝트의 main 아래 resources
				from project.sourceSets.main.resources
				#mapper아래의 xml, templates아래의 모든 파일, common 아래의 모든 파일을 복사
				include 'mapper/**/*.xml', 'templates/**/*', 'common/*', 'messages/*', 'mybatis-config*.xml', 'jwt-key.p12', 'logback.xml', propertyFile
				
				#빌드 결가 위치/config파일 아래 위치시킨다.
				into 'build/libs/config'

                if (isProfile) {
                    rename(propertyFile, 'application.properties')
                }
			}
		}
	}
}
#부트 자르 빌드.끝무렵에 allresources 함수 실행
bootJar.finalizedBy allresources
```

bootjar는 Excutable 하므로 

*** java -jar -cp {config 경로} APP.jar  *** 형태로 classpath를 지정하여 jar파일임에도 불구하고 외부에서 설정정보를 가져올 수 있다.

----

```
project(':main함수를 가지는 프로젝트') {
    bootJar {
        archiveBaseName = 'ROOT'
        manifest.attributes 'Class-Path': 'config/'
        
    }

```

bootjar로 빌드 시 manifaest.attributes 'Class-Path': 'config/' 를 하면 cp가 필요없나? 모르겠다.....
