# SoulChange

* Requirements: Paper 1.21.8+

* Dependency: SkinsRestorer ([Link](https://modrinth.com/plugin/skinsrestorer/versions))

플레이어의 상태(위치, 인벤토리, 스킨 등)를 교체함으로써, 영혼이 바뀌는 효과를 구현한 플러그인입니다.

랜덤 시간 간격 혹은, 플레이어 피격 시에 영혼을 교체하도록 설정할 수 있습니다.


## Features

* 다음의 플레이어 상태들을 교체하여 영혼 교체를 구현합니다.
  * 체력, 배고픔, 경험치 레벨, (물 속에서의) 공기, 불타는 시간, 인벤토리, 핫바 슬롯 위치 (1~9), 현재 위치, 리스폰 위치,
    탑승 상태, 포션 효과, 어그로 끌린 몹, 게임 모드, 스킨

* 다음의 상태들은 교체되지 않습니다.
  * 엔더 상자, 길들이기 상태, 발전 과제 진행 상황, 클라이언트 설정(렌더 거리, 키 지정 등),
    명령어 없이 변경할 수 없는 플레이어 상태 (최대 체력, 기본 이동속도 등)

* 무작위 시간 간격으로 모든 플레이어의 영혼을 교체하는 타이머를 지원합니다.
  * `config.yml`에서 기댓값 조정이 가능합니다.
  * `/sc change`로 실행합니다.

* 플레이어 피격 시 영혼 교체를 실행하는 기능을 지원합니다.
  * `config.yml`에서 활성화 및 설정할 수 있습니다.

* 관전자 모드의 플레이어는 영혼 교체에서 제외됩니다.


## Commands

* `/sc help`: 도움말을 띄웁니다.
* `/sc reload`: config.yml 설정을 불러옵니다.
* `/sc change [start|stop|run]`: 영혼 교체 타이머 시작(start)과 종료(stop), 혹은 즉시 교체(run)를 실행합니다.
* `/sc swap [player1] [player2]`: player1와 player2의 영혼을 맞바꿉니다.


## Configuration

```yaml
# 영혼 교체 타이머는 {interval-seconds}초 마다 {probability}의 확률로 작동합니다.
timer:
  interval-seconds: 30
  probability: 0.33

change-on-damaged:
  # 플레이어 피격시 영혼 교체를 실행할 지 결정합니다.
  enabled: false
  # 피격 당한 플레이어를 공지할 지 결정합니다.
  notify-damagee: false

damage-share:
  # 대미지 공유를 활성화할 지 결정합니다.
  enabled: false
  # 공유될 대미지에 곱해지는 계수입니다. (0 ~ 1의 실수)
  rate: 1.0

# 영혼 교체 공지의 위치를 바꾸거나 비활성화할 수 있습니다. (TITLE, CHAT, NONE)
notification: "TITLE"
```
