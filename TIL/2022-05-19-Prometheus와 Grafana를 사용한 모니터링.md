# TIL 2022-05-19 Prometheus와 Grafana

## 1. prometheus란?
프로메테우스란 오픈소스 시스템 모니터링 및 경고 툴킷
- jvm 정보
- 하드웨어 자원 사용 현황
- 커스터마이즈한 데이터

모니터링 대상이 되는 서버에 exporter 구성, prometheus에서 해당 데이터 pull하여 정보수집

## 2. prometheus 및 Grafana설치

docker-compose.yml
```
version: '2'
services:
 prometheus:
  container_name: local_prometheus
  image: prom/prometheus
  ports:
   - "9090:9090"
  networks:
   - prometheus_bridge
  volumes:
  #프로메테우스 설정
   - "C:/Users/R14280/Desktop/test/prometheusTest/prometheus.yml:/etc/prometheus/prometheus.yml"
   - /var/run/docker.sock:/var/run/docker.sock
   #프로메테우스 데이터 백업
   - "C:/Users/R14280/Desktop/test/prometheusTest/prometheus:/prometheus"

 grafana:
  container_name: local_grafana
  image: grafana/grafana
  ports:
   - "3000:3000"
  volumes: 
   - "C:/Users/R14280/Desktop/test/prometheusTest/grafana:/var/lib/grafana"

  networks:
   - prometheus_bridge
   
    
networks:
 prometheus_bridge:
  external:
   name: prometheus_bridge
   

# docker network create prometheus_bridge 생성 후 사용
```

prometheus.yml

```

global:
  scrape_interval:     60s # 15초마다 매트릭을 수집한다. 기본은 1분이다.
  evaluation_interval: 60s # 15초마다 매트릭을 수집한다. 기본은 1분이다.

alerting:
  alertmanagers:
  - static_configs:
    - targets:

rule_files:

#데이터를 가져올 대상들을 정의
scrape_configs:

  - job_name: 'prometheus'
    static_configs:
    - targets: ['local_prometheus:9090']
     
#  - job_name: 'zookeeper'
#    static_configs:
#    - targets: ['localhost:2181']
    
    
    
  - job_name: 'kafka'
    static_configs:
    - targets: ['local_kafka:1234']
```



## 3. 연동할 kafka에 exporter 세팅

```
version: '2'
services:
 zookeeper:
  container_name: local_zookeeper
  image: wurstmeister/zookeeper
  ports:
   - "2181:2181"
#  environment:
#   - TZ=Asia/Seoul
  networks:
   - default
 kafka:
  container_name: local_kafka
  image: wurstmeister/kafka
  depends_on:
   - zookeeper
  ports:
   - "9092:9092"
   - "1234:1234"
  environment:
   KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
   KAFKA_ADVERTISED_PORT: 9092
   KAFKA_CREATE_TOPICS: "test_topic:1:1"
   KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
	#nodeExporter에서 사용할 yml
   EXTRA_ARGS: "-javaagent:/etc/node_exporter/jmx_prometheus_javaagent-0.12.0.jar=1234:/etc/node_exporter/exporter_config.yml"
#   TZ: Asia/Seoul
  volumes:
   - "C:/Users/R14280/Desktop/test/kafkaTest/kafka_2.13-3.1.0:/root"
   - /var/run/docker.sock:/var/run/docker.sock
   # nodeexporter 다운받아 설치 https://github.com/prometheus/jmx_exporter
   - "C:/Users/R14280/Desktop/test/kafkaTest/node_exporter:/etc/node_exporter"
  networks:
   - default
   - prometheus_bridge
networks:
 prometheus_bridge:
  external:
   name: prometheus_bridge
# docker network create prometheus_bridge 생성 후 사용

```



exporter_config.yml

```
lowercaseOutputName: true

rules:
# Special cases and very specific rules
- pattern : kafka.server<type=(.+), name=(.+), clientId=(.+), topic=(.+), partition=(.*)><>Value
  name: kafka_server_$1_$2
  type: GAUGE
  labels:
    clientId: "$3"
    topic: "$4"
    partition: "$5"
- pattern : kafka.server<type=(.+), name=(.+), clientId=(.+), brokerHost=(.+), brokerPort=(.+)><>Value
  name: kafka_server_$1_$2
  type: GAUGE
  labels:
    clientId: "$3"
    broker: "$4:$5"
- pattern : kafka.coordinator.(\w+)<type=(.+), name=(.+)><>Value
  name: kafka_coordinator_$1_$2_$3
  type: GAUGE

# Generic per-second counters with 0-2 key/value pairs
- pattern: kafka.(\w+)<type=(.+), name=(.+)PerSec\w*, (.+)=(.+), (.+)=(.+)><>Count
  name: kafka_$1_$2_$3_total
  type: COUNTER
  labels:
    "$4": "$5"
    "$6": "$7"
- pattern: kafka.(\w+)<type=(.+), name=(.+)PerSec\w*, (.+)=(.+)><>Count
  name: kafka_$1_$2_$3_total
  type: COUNTER
  labels:
    "$4": "$5"
- pattern: kafka.(\w+)<type=(.+), name=(.+)PerSec\w*><>Count
  name: kafka_$1_$2_$3_total
  type: COUNTER

- pattern: kafka.server<type=(.+), client-id=(.+)><>([a-z-]+)
  name: kafka_server_quota_$3
  type: GAUGE
  labels:
    resource: "$1"
    clientId: "$2"

- pattern: kafka.server<type=(.+), user=(.+), client-id=(.+)><>([a-z-]+)
  name: kafka_server_quota_$4
  type: GAUGE
  labels:
    resource: "$1"
    user: "$2"
    clientId: "$3"

# Generic gauges with 0-2 key/value pairs
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.+), (.+)=(.+)><>Value
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    "$4": "$5"
    "$6": "$7"
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.+)><>Value
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    "$4": "$5"
- pattern: kafka.(\w+)<type=(.+), name=(.+)><>Value
  name: kafka_$1_$2_$3
  type: GAUGE

# Emulate Prometheus 'Summary' metrics for the exported 'Histogram's.
#
# Note that these are missing the '_sum' metric!
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.+), (.+)=(.+)><>Count
  name: kafka_$1_$2_$3_count
  type: COUNTER
  labels:
    "$4": "$5"
    "$6": "$7"
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.*), (.+)=(.+)><>(\d+)thPercentile
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    "$4": "$5"
    "$6": "$7"
    quantile: "0.$8"
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.+)><>Count
  name: kafka_$1_$2_$3_count
  type: COUNTER
  labels:
    "$4": "$5"
- pattern: kafka.(\w+)<type=(.+), name=(.+), (.+)=(.*)><>(\d+)thPercentile
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    "$4": "$5"
    quantile: "0.$6"
- pattern: kafka.(\w+)<type=(.+), name=(.+)><>Count
  name: kafka_$1_$2_$3_count
  type: COUNTER
- pattern: kafka.(\w+)<type=(.+), name=(.+)><>(\d+)thPercentile
  name: kafka_$1_$2_$3
  type: GAUGE
  labels:
    quantile: "0.$4"
```



## 4. Grafana 세팅

1. localhost:3000 접속
   - admin / admin -> skip
2. 좌측 config -> datasources -> addsources
3. prometheus -> url : localhost:9090 -> save & test



## 5. Spring boot에 prometheus 메트릭 추가하기

언제 하냐.. ㅠㅠ
