# 구현과정 상세 설명

## 목차
- [1단계 : 지도 데이터 출력하기](#1단계--지도-데이터-출력하기)
  - [1-1 프로젝트 설명](#1-1-프로젝트-설명)
    - [1) 정적 데이터](#1-1-1-정적-데이터)
    - [2) 동적 데이터](#1-1-2-동적-데이터)
    - [3) 데이터 읽기](#1-1-3-데이터-읽기)
    - [4) 전체 소스](#1-1-4-전체-소스)
  - [1-2 실행방법](#1-2-실행방법)
- [2단계 : 플레이어 이동 구현하기](#2단계--플레이어-이동-구현하기)
  - [2-1 프로젝트 설명](#2-1-프로젝트-설명)
    - [1) 정적 데이터](#2-1-1-정적-데이터)
    - [2) 동적 데이터](#2-1-2-동적-데이터)
    - [3) 게임 관련 기능](#2-1-3-게임-관련-기능)
    - [4) 데이터 출력하기](#2-1-4-데이터-출력하기)
    - [5) 전체 소스](#2-1-5-전체-소스)
  - [2-2 실행방법](#2-2-실행방법)
- [3단계 : 소코반 게임 완성하기](#3단계--소코반-게임-완성하기)
    - [3-1 프로젝트 설명](#3-1-프로젝트-설명)
        - [1) 정적 데이터](#3-1-1-정적-데이터)
        - [2) 동적 데이터](#3-1-2-동적-데이터)
        - [3) 게임 관련 기능](#3-1-3-게임-관련-기능)
        - [4) 데이터를 읽는 기능](#3-1-4-데이터를-읽는-기능)
        - [5) 전체 소스](#3-1-5-전체-소스)
    - [3-2 실행방법](#3-2-실행방법)
- [4단계 : 추가기능 구현](#4단계--추가기능-구현)
    - [4-1 프로젝트 설명](#4-1-프로젝트-설명)
        - [1) 개요](#4-1-1-개요)
        - [2) 정적 데이터](#4-1-2-정적-데이터)
        - [3) 동적 데이터](#4-1-3-동적-데이터)
        - [4) 암호화 기능](#4-1-4-암호화-기능)
        - [5) 파일 입출력 기능](#4-1-5-파일-입출력-기능)
        - [6) 저장, 불러오기 기능](#4-1-6-저장-불러오기-기능)
        - [7) 되돌리기 기능](#4-1-7-되돌리기-기능)
        - [8) 전체 소스](#4-1-8-전체-소스)
    - [4-2 실행방법](#4-2-실행방법)

# 1단계 : 지도 데이터 출력하기

### 1-1 프로젝트 설명
저는 먼저, 요구사항을 통해 구현해야할 개체를 다음과 같이 정리하였습니다.

- **정적 데이터**를 저장하는 개체
- **동적 데이터**를 저장하는 개체
- **데이터를 읽는** 기능

#### 1-1-1 정적 데이터
`Stage 1`, `====`, 그리고 `지도 기호`들은 이미 정해진 데이터입니다.<br>
그래서 **정적 데이터**로 분류하였습니다.<br>
그리고 이 데이터들은 여러 클래스에서 참조할 확률이 높은 **기본 자료형** 데이터라 **enum**으로 선언하였습니다.

#### 1-1-2 동적 데이터
그 다음 `지도`, `좌표`, `지도 크기`, `지도 항목 관련 각종 값들`은 **동적 데이터**로 분류하였습니다.<br>
그래서 외부로 부터 입력을 받아 값이 정해지도록 선언하였습니다.<br>
그 중에서, `지도` 같은 경우, 저장한 지도를 그대로 반환하면 원본이 훼손될 우려가 있습니다.<br>
그래서 각 스테이지에 저장된 지도는 복사하여 반환하도록 개발하였습니다.
(Stage.getCloneChrMap(), Stage.getCloneIntMap())

#### 1-1-3 데이터 읽기
마지막으로 **데이터를 읽는** 기능은는 역할을 추상화 하여 Interface로 먼저 구현하고,실제 기능은 구현체를 통해 동작합니다.<br>
현재는 command line을 통해서 입력받지만, 추후 입력소스가 추가될 가능성이 있기 때문에 확장 가능한 구조로 개발하였습니다.

#### 1-1-4 전체 소스
전체 소스는 다음과 같습니다.<br>
(설명 편의를 위해서 실제 파일순서가 아니라, 관련된 클래스 순서로 나타냅니다.)
```
ROOT
ㄴMain.java : 메인 로직을 실행함(command line 읽기 및 1단계 출력 로직 실행)

// 정적 데이터
ㄴMetaString.java : 메타데이터를 저장함(Stage 시작, Stage 종료 플래그 저장)
ㄴSign.java : sokoban 지도에 포함된 각 항목을 저장함

// 동적 데이터
ㄴPoint.java : x,y 페어를 저장하는 클래스로, 특정 좌표나 크기를 저장하는데 사용함
ㄴStage.java : Stage에 대한 다양한 정보를 저장함
ㄴStageUtils.java : Stage에 대한 유틸 기능을 담고있음

// 데이터를 읽는 기능
ㄴStageReader.java : Stage 정보를 읽는 역할을 추상화한 인터페이스로, 타 입력소스가 추가될 경우 유연한 대처를 위해 사용함
ㄴCmdStageReader.java : StageReader의 구현체로, Comma법nd line을 통해 Stage 정보를 읽어 들임
```

### 1-2 실행방법
1. 소스 클론
```shell
git clone https://gist.github.com/f3fd47327697b966f63ae4d0e28493fa.git
```
2. 자바 소스 컴파일
```shell
cd f3fd47327697b966f63ae4d0e28493fa

javac -classpath . -d build Main.java
```
3. 프로젝트 실행
```shell
# 일반 실행
java -classpath ./build Main

# Exception stack trace 출력하도록 실행
java -classpath ./build Main PRINT_ERROR
```
4. 샘플 데이터 복사
```text
Stage 1
#####
#OoP#
#####
=====
Stage 2
  #######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
 #   O  # 
 ########
```

5. 입력<br>
   4번에서 복사한 데이터를 cmd에 붙여넣고 엔터키를 입력한 다음,<br>
   **한번 더** 엔터를 입력하면 입력이 종료되고 결과가 출력됩니다.


6. 결과
```shell
> java -classpath ./build Main
Stage 1
#####
#OoP#
#####
=====
Stage 2
  #######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
 #   O  # 
 ########
 
Stage 1

#####
#OoP#
#####

가로 크기 : 5
세로 크기 : 3
구멍의 수 : 1
공의 수 : 1
플레이어 위치 (2, 4)

Stage 2

  #######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
 #   O  # 
 ########

가로 크기 : 11
세로 크기 : 7
구멍의 수 : 4
공의 수 : 4
플레이어 위치 (4, 6)


Process finished with exit code 0
```

## 2단계 : 플레이어 이동 구현하기

### 2-1 프로젝트 설명

사용자의 이동을 구현하는 기능을 구현하기에 앞서, 이번에도 구현해야할 기능들을 분류해봤습니다.

- **정적 데이터**를 저장하는 개체<br>
    `명령`, `방향`, `메세지`
- **동적 데이터**를 저장하는 개체<br>
    `사용자의 위치`
- **게임 관련** 기능<br>
    `게임 실행(스테이지 선택)`, `스테이지 플레이`
- **데이터를 출력하는** 기능
    데이터 `출력 기능`

#### 2-1-1 정적 데이터
먼저, **정적 데이터**는 위에 명시한 항목들로 생각하여, *UserCommand.java*에 enum으로 개발하였습니다.<br> 
그리고 각 `명령`에 `방향`과 `메세지`가 매핑되기 때문에, 각 enum 요소에 `direction`과 `message`를 부여하였습니다.

#### 2-1-2 동적 데이터
그 다음, **동적 데이터**는 `사용자의 위치` 입니다. 위치 데이터도 이전에 개발했던 *Point.java*와 마찬가지로 x,y 쌍을 저장합니다.<br>
하지만, *Point.java*는 정적 데이터로 설계되었기 때문에, 이를 상속하여 동적 데이터로 활용할 수 있는 *Position.java*를 개발하였습니다.

#### 2-1-3 게임 관련 기능
게임 관련 기능으로는 `게임 실행(스테이지 선택)`과, `스테이지 플레이`로 구분하였습니다.<br>
이번 2022 테스트 문제 개요에 첨부된 [소코반게임](https://www.cbc.ca/kids/games/play/sokoban) 에서는 `Play`버튼을 누르면
바로 스테이지를 `플레이`하는 것이 아니라, 선택할 스테이지를 보여줍니다.<br>
그리고 `스테이지를 선택`하여 `플레이` 합니다. 여기서 저는 게임 `플레이`를 포함하는 개념이 필요하다고 생각하였습니다.<br>
그래서 `스테이지를 선택`하는 기능은 *SokobanGame.java* 그리고 `플레이`하는 기능은 *Play.java*로 구현하였습니다.

#### 2-1-4 데이터 출력하기
`출력 기능` 또한, 어느 정도 정형화된 역할이기 때문에 *StageWriter.java* 인터페이스로 추상화하고, 이를 *CmdStageWriter.java*로 구현하였습니다.


#### 2-1-5 전체 소스
전체 소스는 다음과 같습니다.<br>
(설명 편의를 위해서 실제 파일순서가 아니라, 관련된 클래스 순서로 나타냅니다.)
```
ROOT
# 추가된 파일
// 정적 데이터
ㄴUserCommand.java : 사용자 입력 및 이동방향, 메세지를 저장함

// 동적 데이터
ㄴPosition.java : Point를 상속한 클래스로, 위치를 수정할 수 없는 제한을 해결하기 위해 사용함

// 게임 관련 기능
ㄴSokobanGame.java : sokoban 게임을 실행하는 주체로, 스테이지들과 클리어 여부를 저장함
ㄴPlay.java : sokobanGame의 각 스테이지를 플레이하는 로직을 담당하는 클래스로, 플레이에 필요한 정보들을 저장함

// 출력 관련 기능
ㄴStageWriter.java : Stage 정보를 출력하는 역할을 추상화한 인터페이스 
ㄴCmdStageWriter.java : StageWriter의 구현체로, Command Line을 통해 Stage 정보를 출력함
--------------------------------------------------------------------------------------------------
# 기존 파일들

ㄴMain.java : 메인 로직을 실행함(command line 읽기 및 1단계 출력 로직 실행)

// 정적 데이터
ㄴMetaString.java : 메타데이터를 저장함(Stage 시작, Stage 종료 플래그 저장)
ㄴSign.java : sokoban 지도에 포함된 각 항목을 저장함

// 동적 데이터
ㄴPoint.java : x,y 페어를 저장하는 클래스로, 특정 좌표나 크기를 저장하는데 사용함
ㄴStage.java : Stage에 대한 다양한 정보를 저장함
ㄴStageUtils.java : Stage에 대한 유틸 기능을 담고있음

// 데이터를 읽는 기능
ㄴStageReader.java : Stage 정보를 읽는 역할을 추상화한 인터페이스로, 타 입력소스가 추가될 경우 유연한 대처를 위해 사용함
ㄴCmdStageReader.java : StageReader의 구현체로, Command line을 통해 Stage 정보를 읽어 들임
```

### 2-2 실행방법
> [1. 소스 클론 ~ 3. 프로젝트 실행] 까지는 [이전](#1-2-실행방법)과 동일

4. 샘플 데이터 복사
```text
Stage 2
  #######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
 #   O  # 
 ########
```

5. 입력<br>
   4번에서 복사한 데이터를 cmd에 붙여넣고 엔터키를 입력한 다음,<br>
   **한번 더** 엔터를 입력하면 맵이 입력되고 2 스테이지 플레이 화면이 출력됩니다.

6. 결과
```shell
> java -classpath ./build Main
Stage 2
  #######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
 #   O  # 
 ########

  #######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
 #   O  # 
 ########

SOKOBAN> dwd

명령어: d
D: 오른쪽으로 이동합니다.
  #######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
 #   O  # 
 ########


명령어: w
W: 위로 이동합니다.
  #######
###  O  ###
#    oP   #
# Oo   oO #
###  o  ###
 #   O  # 
 ########


명령어: d
D: 오른쪽으로 이동합니다.
  #######
###  O  ###
#    o P  #
# Oo   oO #
###  o  ###
 #   O  # 
 ########

SOKOBAN> ddd

명령어: d
D: 오른쪽으로 이동합니다.
  #######
###  O  ###
#    o  P #
# Oo   oO #
###  o  ###
 #   O  # 
 ########


명령어: d
D: 오른쪽으로 이동합니다.
  #######
###  O  ###
#    o   P#
# Oo   oO #
###  o  ###
 #   O  # 
 ########


명령어: d
(경고!) 해당 명령을 수행할 수 없습니다!!
  #######
###  O  ###
#    o   P#
# Oo   oO #
###  o  ###
 #   O  # 
 ########
```

## 3단계 : 소코반 게임 완성하기

### 3-1 프로젝트 설명

소코반 게임을 완전히 완성하기 위해서는 새로운 데이터와 기능들이 필요했습니다.

- **정적 데이터**를 저장하는 개체<br>
  `공이 구멍에 들어간 상태`, `R(초기화) 명령어`
- **동적 데이터**를 저장하는 개체<br>
  `플레이어 이동 횟수`, `현재 구멍에 들어간 공 개수`
- **게임 관련** 기능<br>
  `스테이지 클리어`, `다음 스테이지`, `전체 클리어 확인`
- **데이터를 읽는** 기능<br>
  `파일 데이터 입력`

#### 3-1-1 정적 데이터
3단계에서 `공이 구멍에 들어간 상태`와 `R 명령어`를 추가하라는 요구사항이 있었습니다.<br>
`공이 구멍에 들어간 상태`는 지도에 표시될 기호이기 때문에 *Sign.java*에 추가하였습니다.<br>
그리고 `R 명령어`를 추가하려니 플레이어 이동 명령과는 다른 성격인 명령이 계속 추가되는 것 같아 이 둘을 분리하였습니다.<br>
그리서 이동 명령은 *PlayerCommand.java*에 추가하고, `R 명령어`는 `Q 명령어`와 함께 *SystemCommand.java*에 추가하였습니다. 

#### 3-1-2 동적 데이터
추가로 필요하게된 동적 데이터는 `플레이어 이동 횟수`, `구멍에 들어간 공 개수` 입니다.<br>
스테이지를 플레이하는 중에 변하는 데이터이기 때문에, 플레이를 담당하는 *Play.java*에 추가하였습니다.

#### 3-1-3 게임 관련 기능
시작시에 제일 처음 스테이지에서 시작하도록 구성하였고, 그리고 나서는 `스테이지 클리어` 기능이 필요했습니다.<br>
클리어 여부는 플레이를 진행하는 개체의 내부에서 판단 후, 외부로 전송할 필요가 있었습니다.<br>
그 이유는, [소코반게임](https://www.cbc.ca/kids/games/play/sokoban) 현재 스테이지를 클리어 해야 다음 스테이지로 이동할 수 있기 때문입니다.<br>
그래서 `스테이지 클리어`를 **판단**하는 기능은 *Play.java*에, 이를 확인하여 `다음 스테이지`를 진행시키는 로직은 *SokobanGame.java*에 추가하였습니다.<br>
그리고 마지막 스테이지까지 모두 클리어 하면, *SokobanGame.java*에서`전체 클리어 확인` 단계를 거친 후 축하메세지를 출력하게 됩니다.

#### 3-1-4 데이터를 읽는 기능
지금 까지는 Command line을 통해서 입/출력을 모두 수행했지만, 이번 단계에서는 File을 통한 입력을 구현해야했습니다.<br>
기존에 입력 소스가 추가될 것을 고려하여 만들었던 인터페이스를 상속하여 *FileStageReader.java*로 파일 입출력 기능을 구현하였습니다.

#### 3-1-5 전체 소스
전체 소스는 다음과 같습니다.<br>
(설명 편의를 위해서 실제 파일순서가 아니라, 관련된 클래스 순서로 나타냅니다.)
```
ROOT
# 추가된 파일

// 정적 데이터
ㄴUserCommand.java -> PlayerCommand.java : 사용자의 입력 중 이동 방향에 대한 명령어만 저장함
ㄴSystemCommand.java : 사용자의 입력 중 게임 시스템 상호작용에 대한 명령어만 저장함

// 입출력 기능
FileStageReader.java : StageReader 구현체로, File을 통해 Stage 정보를 읽어 들임

--------------------------------------------------------------------------------------------------
# 기존 파일들

ㄴMain.java : 메인 로직을 실행함(command line 읽기 및 1단계 출력 로직 실행)

// 정적 데이터
ㄴMetaString.java : 메타데이터를 저장함(Stage 시작, Stage 종료 플래그 저장)
ㄴSign.java : sokoban 지도에 포함된 각 항목을 저장함

// 동적 데이터
ㄴPoint.java : x,y 페어를 저장하는 클래스로, 특정 좌표나 크기를 저장하는데 사용함
ㄴPosition.java : Point를 상속한 클래스로, 위치를 수정할 수 없는 제한을 해결하기 위해 사용함
ㄴStage.java : Stage에 대한 다양한 정보를 저장함
ㄴStageUtils.java : Stage에 대한 유틸 기능을 담고있음

// 게임 관련 기능
ㄴSokobanGame.java : sokoban 게임을 실행하는 주체로, 스테이지들과 클리어 여부를 저장함
ㄴPlay.java : sokobanGame의 각 스테이지를 플레이하는 로직을 담당하는 클래스로, 플레이에 필요한 정보들을 저장함

// 입출력 기능
ㄴStageReader.java : Stage 정보를 읽는 역할을 추상화한 인터페이스로, 타 입력소스가 추가될 경우 유연한 대처를 위해 사용함
ㄴCmdStageReader.java : StageReader의 구현체로, Command line을 통해 Stage 정보를 읽어 들임
ㄴStageWriter.java : Stage 정보를 출력하는 역할을 추상화한 인터페이스 
ㄴCmdStageWriter.java : StageWriter의 구현체로, Command Line을 통해 Stage 정보를 출력함
```

### 3-2 실행방법
> [1. 소스 클론 ~ 3. 프로젝트 실행] 까지는 [이전](#1-2-실행방법)과 동일

4. 결과
```shell
> java -classpath ./build Main
Stage: 1
####
#P #
# o##
#  O#
#####

...

Stage: 2

...

Stage: 5

...

이동 횟수 : 92
성공!! 축하합니다.
클리어한 스테이지 수 5/5

마지막 스테이지 입니다.

Process finished with exit code 0
```

## 4단계 : 추가기능 구현

### 4-1 프로젝트 설명
이번 단계에서는 `저장하기`, `불러오기` 그리고 `되돌리기`, `되돌리기 취소` 기능이 필요했습니다.

- **정적 데이터**를 저장하는 개체<br>
  `저장 명령`, `불러오기 명령`, `되돌리기 명령`, `되돌리기 취소 명령`
- **동적 데이터**를 저장하는 개체<br>
  `게임 진행상황`
- **암호화**기능<br>
  `암호화`, `복호화`
- **파일 입/출력**기능<br>
  `파일 데이터 출력`, `암호화 파일 데이터 출력`, `암호화 파일 데이터 입력`
- **저장 불러오기**기능<br>
- **되돌리기**기능<br>

#### 4-1-1 개요
이번 4단계에서는 고급(?)기능들이 추가되었습니다.<br>
게임 플레이 관련 기능으로는 `저장`, `불러오기`, `되돌리기`, `되돌리기 취소`가 있고,<br>
게임 시스템 관련 기능으로는 `암호화된 맵 정보 입/출력 기능`이 추가되었습니다.<br>

이 기능들을 구현하기 위해 다시 위 목록처럼 분류해보았습니다.<br>

#### 4-1-2 정적 데이터
먼저, **정적 데이터**로는 각종 명령들을 추가했습니다. 그런데, 이전에는 대소문자를 확인하지 않도록 만들어 놨다가 낭패를 봤습니다.<br>
이번 추가 요구사항의 `저장 명령`이 대문자 'S'라서 기존 `아래 명령`인 's'와 중복되었고, `되돌리기 명령`, `되돌리기 취소 명령` 또한<br>
소문자 u가 `되돌리기`, 대문자 U가 `되돌리기 취소`라서 대소문자를 구분하도록 변경하였습니다.<br> 
그래서 3단계까지는 상하좌우 커맨드인 wsad 커맨드가 대문자도 됐었지만, **이번 4단계 부터는 소문자만 인식합니다.**<br>

#### 4-1-3 동적 데이터
그 다음은 **동적 데이터**로는 `게임 진행상황`입니다.<br>
기존에는 게임 플레이 로직이 있는 *Play.java*에 저장되었지만, 여러 슬롯에 `게임 진행상황`을 하는 기능을 *Play.java*에<br>
추가하는 것은 좋은 구조가 아니라고 판단하였습니다. 그래서 먼저 *PlayStatus.java*로 `게임 진행상황`을 분리한 다음,<br>
*PlayStatusStore.java*라는 store객체를 통해 저장하고 불러오도록 구현하였습니다.

#### 4-1-4 암호화 기능
**암호화** 기능은 **양방향 암호화** 알고리즘인 `AES256`을 선택하였습니다.<br>
Stage 정보를 `암호화`하여 파일로 저장하고, 게임 실행시에 `복호화` 해야하기 때문에 **양방향 암호화** 알고리즘을 선택하였습니다.<br>
(*map_enc.txt* 참고)

#### 4-1-5 파일 입출력 기능
이번단계에서는파일 출력기능 및 암호화 입/출력이 필요했습니다. 그래서 암호화 클래스 *AES256Encrypter.java*와<br>
*StageWriter.java*, *StageReader.java*를 상속한 암호화 입/출력 클래스들을 개발하였습니다.<br>

#### 4-1-6 저장, 불러오기 기능
`저장`, `불러오기` 기능을 개발하기 전에, 먼저 `진행상황` 데이터를 *Play.java*에서 *PlayStatus.java*로 분리하였습니다.<br>
그리고 이를 저장할 *PlayStatusStore.java*를 *Play.java*에서 참조하여 기능을 수행하도록 개발하였습니다.

#### 4-1-7 되돌리기 기능
`되돌리기`, `되돌리기 취소` 기능은 Stack을 통해서 구현했습니다. 한 턴을 진행한 정보를 doStack에 넣고,<br>
`되돌리기` 명령이 들어오면 저장한 정보에 따라 플레이어와 공을 이동시킨 뒤, undoStack에 해당 명령을 넣었습니다.
그리고 `되돌리기 취소` 명령이 들어오면, undoStack에 저장된 명령을 꺼내서 다시 플레이어와 공을 이동시키도록 개발하였습니다.

#### 4-1-8 전체 소스
전체 소스는 다음과 같습니다.<br>
(설명 편의를 위해서 실제 파일순서가 아니라, 관련된 클래스 순서로 나타냅니다.)

```text
ROOT
# 추가된 파일

// 동적 데이터
ㄴPlayStatus.java : 진행중인 스테이지의 진행상황을 저장함
ㄴTurn.java : 사용자가 입력하여 이동한 PLAYER, BALL의 좌표와 명령을 저장함

// 암호화 관련 기능
ㄴAES256Encrypter.java : AES256 암/복호화 기능을 제공함

// 입출력 기능
ㄴFileStageWriter.java : Stage 정보를 텍스트 파일로 출력함
ㄴAES256FileStageReader.java : AES256으로 암호화된 파일을 복호화하여 Stage 객체를 만듦
ㄴAES256FileStageWriter.java : Stage 정보를 AES256으로 암호화하여 텍스트 파일로 출력함

// 저장, 불러오기 기능
ㄴPlayStatusStore.java : 스테이지 진행상황을 저장할 공간을 제공함  

--------------------------------------------------------------------------------------------------
# 기존 파일들

ㄴMain.java : 메인 로직을 실행함(command line 읽기 및 1단계 출력 로직 실행)

// 정적 데이터
ㄴMetaString.java : 메타데이터를 저장함(Stage 시작, Stage 종료 플래그 저장)
ㄴSign.java : sokoban 지도에 포함된 각 항목을 저장함
ㄴPlayerCommand.java : 사용자의 입력 중 이동 방향에 대한 명령어만 저장함
ㄴSystemCommand.java : 사용자의 입력 중 게임 시스템 상호작용에 대한 명령어만 저장함

// 동적 데이터
ㄴPoint.java : x,y 페어를 저장하는 클래스로, 특정 좌표나 크기를 저장하는데 사용함
ㄴPosition.java : Point를 상속한 클래스로, 위치를 수정할 수 없는 제한을 해결하기 위해 사용함
ㄴStage.java : Stage에 대한 다양한 정보를 저장함
ㄴStageUtils.java : Stage에 대한 유틸 기능을 담고있음

// 게임 관련 기능
ㄴSokobanGame.java : sokoban 게임을 실행하는 주체로, 스테이지들과 클리어 여부를 저장함
ㄴPlay.java : sokobanGame의 각 스테이지를 플레이하는 로직을 담당하는 클래스로, 플레이에 필요한 정보들을 저장함

// 입출력 기능
ㄴStageReader.java : Stage 정보를 읽는 역할을 추상화한 인터페이스로, 타 입력소스가 추가될 경우 유연한 대처를 위해 사용함
ㄴCmdStageReader.java : StageReader의 구현체로, Command line을 통해 Stage 정보를 읽어 들임
ㄴFileStageReader.java : StageReader 구현체로, File을 통해 Stage 정보를 읽어 들임
ㄴStageWriter.java : Stage 정보를 출력하는 역할을 추상화한 인터페이스
ㄴCmdStageWriter.java : StageWriter의 구현체로, Command Line을 통해 Stage 정보를 출력함
```

### 4-2 실행방법

> [1. 소스 클론 ~ 3. 프로젝트 실행] 까지는 [이전](#1-2-실행방법)과 동일

4. 결과

```shell
# 저장, 불러오기 기능 --------------------------------------------------------
> java -classpath ./build Main
Stage 1
####
#P #
# o##
#  O#
#####

SOKOBAN> d

명령어: d
d: 오른쪽으로 이동합니다.
####
# P#
# o##
#  O#
#####

SOKOBAN> 1S
1번 세이브 슬롯에 진행상황을 저장합니다.
저장 성공!
####
# P#
# o##
#  O#
#####

SOKOBAN> r
스테이지를 초기화 합니다.
####
#P #
# o##
#  O#
#####

SOKOBAN> 1L
1번 세이브 슬롯에서 진행상황을 불러옵니다.
####
# P#
# o##
#  O#
#####

SOKOBAN> 

# 되돌리기, 되돌리기 취소 기능 --------------------------------------------------------

> java -classpath ./build Main
Stage 1
####
#P #
# o##
#  O#
#####

SOKOBAN> ds

명령어: d
d: 오른쪽으로 이동합니다.
####
# P#
# o##
#  O#
#####


명령어: s
s: 아래로 이동합니다.
####
#  #
# P##
# oO#
#####

SOKOBAN> u
한 턴 되돌리기: s
####
# P#
# o##
#  O#
#####

SOKOBAN> U
s: 아래로 이동합니다.
####
#  #
# P##
# oO#
#####

SOKOBAN> 
```

----

Made by [@jinan159](https://github.com/jinan159)