# JS 쓰면서 자주 사용하는 로직들

## Object 리스트에서 특정 값을 가지는 Object 제거
```
// tartget은 Object를 가지는 리스트
// target의 Object에서 name이 '김만기'인 오브젝트를 모두 제거하기

let idx = -1
while((idx = target.findIndex(x => x.name === '김만기') > -1)){
  target.splice(idx, 1)
  // while 구문을 타며 인덱스를 찾아줌, splice를 통해 해당 값을 잘라냄
  // return 값은 잘라낸 Object 1개이며 target에서는 잘라낸 나머지를 가지고 있음
}

``
