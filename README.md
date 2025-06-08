# Soul Change

* Supported minecraft version: 1.21.1 only (Paper server recommended)

* Dependency: DisguiseAPI ([Link](https://www.spigotmc.org/resources/disguiseapi.103942/))

플레이어의 상태(위치, 인벤토리, 스킨 등)를 교체함으로써, 영혼이 바뀌는 효과를 구현한 플러그인입니다.

랜덤 시간 간격으로 혹은, 누군가 대미지를 받을 때 영혼을 교체하도록 설정할 수 있습니다.


## LICENSE

본 플러그인은 개인적 용도에 한해 무료로 배포됩니다. 유튜브 영상, 방송 등의 상업적 용도 사용 시 제작자(kwondy0500@naver.com)에게 문의 바랍니다.


## Features

* 영혼 교체 시 다음의 플레이어 상태들이 교체됩니다.
    * 체력, 배고픔, 레벨, (물 속에서의) 공기, 불타는 시간, 인벤토리, 핫바 슬롯 위치 (1~9), 현재 위치, 리스폰 위치,
      탑승 상태, 포션 효과, 어그로 끌린 몹, 게임 모드, 스킨과 이름표

* 다음과 같은 상태들은 교체되지 않습니다.
  * 엔더 상자, 길들이기 상태, 발전 과제 진행 상황, 클라이언트 세팅(렌더 거리, 키 지정 등),
    명령어 없이 변경할 수 없는 플레이어 특성 (최대 체력, 기본 이동속도 등)

* 랜덤한 시간 간격으로 모든 플레이어의 영혼을 교체하는 타이머를 지원합니다.
  * config.yml에서 설정할 수 있습니다.
  * 서버 재시작 혹은 reload 시 타이머를 다시 시작해야 하니 주의하세요.

* 대미지를 입을 시 영혼이 교체되는 기능, 대미지 공유 기능을 설정할 수 있습니다.
  * config.yml에서 설정할 수 있습니다.
  * 대미지 공유는 모든 플레이어에게 '고정 대미지'로 들어가며, 대미지 공유 계수(0.0 ~ 1.0)를 설정할 수 있습니다.

* 관전자 모드의 플레이어는 영혼 교체에서 제외됩니다.


## Commands

* /sc reload: config.yml 설정을 불러옵니다.
* /sc change \[start|stop|run\]: 타이머 시작(start)과 종료(stop), 혹은 즉시 교체(run)를 실행합니다.
* /sc skin \[player1|reset\]: player1으로 변장하거나 변장을 해제(reset)합니다.
* /sc skin \[player1\] \[player2|reset\]: player1을 player2로 변장시키거나 변장을 해제합니다.


## Config

```yaml
# 영혼 교체 타이머는 {interval-seconds}초 마다 {probability}의 확률로 작동합니다.
timer:
  interval-seconds: 30   # 초 단위로 입력해주세요.
  probability: 0.33      # 0 ~ 1의 실수를 입력하세요.

# 플레이어 피격시 영혼 교체를 실행할 지 결정합니다.
change-on-damaged: false

# 대미지 공유를 활성화할 지 결정합니다.
damage-share: false

# 공유될 대미지에 곱해지는 계수입니다.
damage-share-rate: 1.0  # 0 ~ 1의 실수를 입력하세요.

# 영혼 교체 공지의 위치를 바꾸거나 비활성화할 수 있습니다.
notification: "TITLE" # TITLE, CHAT, NONE
```
