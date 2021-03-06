# Global Clipboard Server

## Global Clipboard
Global Clipboard는 다수의 사용자가 클립보드를 이용하여 데이터를 간편하게 주고 받을 수 있는 Server-Client
구조의 데이터 전송 플랫폼입니다. 사용자는 클립보드를 이용한 복사, 붙여넣기 인터페이스와 이에 접근 가능한 단축키 (Ctrl C, Ctrl V)를 이용하여 다른 번거로운 과정 없이 간편하게 데이터를 주고 받을 수 있습니다. 따라서 다른 사용자가 복사한 데이터를 자신이 붙여넣는 것 처럼 사용할 수 있습니다. 클립보드를 이용하기 때문에 클립보드에 복사할 수 있는 모든 종류의 데이터(Text, Capture Image, File)를 전송할 수 있습니다.

자세한 내용은 [Wiki](https://github.com/Team-Sprout/Clipcon-Server/wiki) 참조.

## ClipCon
ClipCon은 Global Clipboard에서 동작하는 클라이언트 어플리케이션의 이름입니다. 현재 본 프로젝트에서는 윈도우와 안드로이드에서 구동되는 어플리케이션을 개발하였습니다. ClipCon에 대한 자세한 내용은 다음을 참고하여 주시기 바랍니다.
* 윈도우: [Windows ClipCon GitHub](https://github.com/team-sprout/clipcon-client)
* 안드로이드: [Android ClipCon GitHub](https://github.com/team-sprout/clipcon-AndroidClient)

# 구조
**Global Clipboard** Server는 Apache Tomcat을 이용하여 웹 서버를 구축하여 사용자가 업로드한 데이터를 저장하고 요청한 데이터를 전송합니다. 또한 서버의 상태를 먼저 알려주기 위하여 Websocket을 이용한 push기능을 구현하였습니다. 개발에는 Tomcat 8.0 버전을 이용하여 개발하였고 현재 Windows 기반의 PC에서 임시 서버를 구동 중입니다.

## 특징
* **계정 불필요**
  -  번거로운 가입, 로그인 절차가 없습니다.
* **직관적인 조작**
  - 사용자에게 매우 친숙한 *클립보드* 라는 인터페이스를 이용하여 빠르고 직관적으로 사용할 수 있습니다. 조작의 단순화로 사용자의 생산성을 증가시킵니다.
* **즉석 데이터 편집 및 전송 가능**
  - *클립보드* 를 이용하기 때문에 파일 형태가 아닌 instant한 단순한 텍스트, 캡처 이미지와 같은 데이터를 손쉽게 주고 받을 수 있습니다.

## 유용성
Global Clipboard는 다수의 다바이스 사이에서 데이터를 주고 받는 상황이라면 어디서든 활용가능하지만, 신뢰있는 사용자간에 전송할 데이터가 간헐적으로 발생할 때 가장 효과적으로 사용할수 있습니다.
* 1인 2PC 이상 사용자의 PC간 데이터 교환
  * 예) 작업용 데스크탑과 개인용 노트북 간 텍스트 또는 파일 복사/붙여넣기 등
* 다수의 사용자와 협업 시, 데이터 전송 및 교환
  * 예1) 팀원과 함께 발표 자료를 만들 때 적절한 캡처 이미지 복사/붙여넣기
  * 예2) 교수자가 학생들에게 즉석에서 필요한 수업자료를 클립보드를 이용하여 배포 등
