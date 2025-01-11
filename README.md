# Soul Change

* Supported minecraft version: 1.21 only (Paper server recommended)

* Dependency: DisguiseAPI ([Link](https://www.spigotmc.org/resources/disguiseapi.103942/))


플레이어의 상태(체력, 인벤토리, 스킨 등)를 교환하여 마치 영혼(클라이언트)이 교환되는 듯한 효과를 주는 플러그인입니다.
랜덤한 시간 간격으로 서버의 모든 플레이어의 영혼을 뒤바꾸는 타이머를 지원합니다.

※ 허락되지 않은 상업적 이용을 금합니다.

## Features

* 다음과 같은 플레이어의 상태를 바꾸어 영혼 교환을 구현하였습니다.
    * 체력
    * 배고픔
    * 레벨
    * (물 속에서의) 공기
    * 불타는 시간
    * 인벤토리
    * 현재 위치
    * 리스폰 위치
    * 포션 효과
    * 어그로 끌린 몹
    * 게임 모드
    * 스킨과 이름표


* 다음과 같은 상태들은 교환되지 않습니다.
  * 이동 중인 방향 및 속도
  * 도전 과제 진행 상황
  * 핫바 슬롯 위치
  * 렌더 거리, 키 지정 등의 모든 클라이언트 세팅


* 30초마다 33%의 확률로 모든 플레이어의 영혼을 교환하는 랜덤 타이머를 지원합니다.
  * 서버 재시작 혹은 reload 시 타이머를 다시 시작해야 하니 주의하세요.
  * config.yml에서 기본 시간 간격과 확률을 수정할 수 있습니다.

## Commands

* /change : 즉시 모든 플레이어의 영혼을 뒤바꿉니다.
* /change \[start|stop\] : 30초마다 33%의 확률로 모든 플레이어의 영혼을 교환하는 랜덤 타이머를 시작하고 중지합니다.


* /skin \[nickname\] : 플러그인 작동 중에 오류가 발생한다면, 해당 명령어로 'nickname'에 해당하는 스킨과 이름표로 직접 변장할 수 있습니다.
* /skin \[nickname1\] \[nickname2\] : 'nickname1'으로 변장한 플레이어를 'nickname2'로 변장하게 합니다.
