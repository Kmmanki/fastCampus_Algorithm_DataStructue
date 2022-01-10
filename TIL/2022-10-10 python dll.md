# 파이썬에서 C# DLL 사용하는방법
- 프로젝트 도중 C#으로 구현된 DLL을 통하여 통신하는 방법이 필요 했으나 C#을 몰라서 찾던 중 Python에서 C#의 DLL 사용하는 방법이 있었다.
- 해당 인스턴스를 라이브러리로부터 받고 callBakc함수를 만들어 Delegate에 넘겨주면 응답 받을 시 실행시킬 함수를 정의할 수 있었다.

```
import clr
clr.AddReference("./outterLibrary")
from outterLibrary import outterClass
===중략 ==

def eventListner(responseData):
  print(eventListner)

instance = outterClass.Instance()
instance.OnRead = outterClass.OnReadDelegate(eventListner)
```
