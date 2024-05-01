# lol
spring project

# 1. 사용자 인증 및 권한 처리

- 인증 방식 : JWT
- 권한
    - admin(관리자) : 모든 접근 가능, 모든 글 수정, 삭제 권한
    - User(회원) : 읽기 권한, 자신의 글 수정 및 삭제 권한\
    - 모든 사람: 읽기 권한

### 로그인, 회원 가입

- 이름, 아이디, 비밀번호, 소환사 태그, 소환사 닉네임, 이메일, 전화번호 제공하여 회원 가입이 가능하다.
- 소환사 태그는 #KR , 소환사 닉네임은 롤 닉네임을 의미한다.
- 소환사 닉네임은 라이엇 API로 가져올 때 Puuid(암호화 된 긴 고유 아이디) 로 받아서 중복 체크 한다.
- 중복된 아이디, 소환사 닉네임은 가입 불가
- 소환사 닉네임 인증 시나리오 만들기(README)
    - 라이엇 로그인 연동 API가 기술적(라이엇 회사의 승인을 받아야 함)으로 불가능하기 때문에,  사용 가능한 라이엇API(PuuId)로 회원가입을 진행 한다
    - 라이엇 로그인 API가 서비스화가 되면 PuuId를 가져와 회원가입을 하지 않고 라이엇 로그인 API를 활용하여 회원가입 , 로그인이 가능하다.
- 로그인
    - 아이디, 비밀번호를 사용하여 로그인한다.
 
    - ### 마이 페이지

- 유저가 개인정보를 확인하고 수정할 수 있는 마이 페이지가 존재한다.
- 마이 페이지에서 개인정보를 볼 수 있다.
    - 아이디, 비밀번호, 소환사 닉네임, 이메일, 전화번호
- 비밀번호와 소환사 닉네임은 수정 페이지에서 수정이 가능하다.
    - 비밀번호 수정 페이지 에서 비밀번호 확인을 거친다.
    - 소환사 닉네임 수정 페이지에서 소환사 닉네임을 변경할 때 라이엇 api로 실제 있는 아이디인지 확인한다.
- 이메일과 전화번호는 마이 페이지 내에서 수정이 가능하다.

# 2. 사용자 정보

### 유저 검색 기능

- 모든 전적은 솔로 랭크를 기준으로 한다.
- 유저 소환사 닉네임을 검색하면 연결되는 유저 페이지가 존재한다.
- 유저를 검색하면 다양한 정보가 나온다
    1. 소환 닉네임, 소환사 태그
        - 회원가입시 입력한 소환사 태그 (#KR) , 소환사 닉네임을 띄워준다.
    2. 프로필 이미지
        - 라이엇 Api 를 이용하여 실제 롤 인게임의 프로필 이미지를 가져온다. (op.gg 프로필 참고)
 
  3. 최근 20 게임 전적
    - 최근 20 게임에 대한 전적을 나타낸다.
        - 포함되는 정보는 다음과 같다.
        - 지난 시간, 승패, 게임 시간, 프로필 이미지, K/D/A, 평점, 킬관여율, 제어와드, CS(분당 CS), 게임 참여 유저(10명) 닉네임
     
- # 3. 듀오 게시판

### 듀오 게시판

- 듀오 게시판에서 유저가 함께 게임할 듀오를 구하는 게시글을 작성할 수 있다.
- 게스트도 글 조회가 가능하다.

### 게시글

- 작성자(유저)가 듀오를 찾는 게시글을 작성할 수 있다.
- 게시글 목록은 최신 순으로 나열한다.
    - 게시글 필터를 설정할 수 있다.
        - 찾는 포지션 별 게시글 열람(상관없음, 탑, 정글, 미드, 원딜, 서폿)
        - 티어 별 게시글 열람(티어 전체, 아이언, 브론즈, 실버, 골드, 플래티넘, 다이아, 챌린저)
- 게시글 목록에는 다음과 같은 내용이 포함된다.
    - 프로필 이미지
    - 소환사 닉네임(기본 소환사 닉네임 + 태그)
 
    ---
    
    - 주 포지션
    - 티어
    - 찾는 포지션
    - 한 마디
    - 게시글 등록 일시
 
- **라이엇 api를 이용**

---

- 최근 선호 챔피언 3개
- 승률
- KDA

- 작성자의 게시글 매칭 상태가 대기 중일 때에는 , 글 작성 권한이 주어지지 않는다.
    - 중복 매칭 및 매칭 테러를 위해 매칭 대기 상태가 1 개 이상일 경우에는 글을 작성할 수 없다.
    - 단, 매칭 완료된 게시글은 삭제되지 않는다.
    - 작성자는 업데이트 버튼을 통하여 게시글을 최상단으로 다시 올릴 수 있다( 등록 시간 최근 시간으로 변경)

### 작성자

- 게시글을 작성할 수 있다.
    - 게시글 작성 페이지
        - 나의 포지션, 찾는 포지션, 한마디를 입력하여 작성 등록을 할 수 있다.
    
- 듀오 게시판에서 내 게시글 목록에 들어갈 수 있다.
    - 자신이 작성한 글을 내 게시글 목록에서 확인할 수 있다.
    - 내 게시글 목록에서 확인하는 정보는 듀오 게시판의 게시글 목록과 같다.
        - 프로필, 소환사 닉네임, 주 포지션, 찾는 포지션, 한 마디, 등록 일시, 최근 선호 챔피언, 승률, KDA
     
    - - 게시글 상세 페이지
    - 내가 쓴 게시글을 확인할 수 있다.
    - 게시글 상세 페이지에서 신청자들 목록을 확인 할 수 있다.
        - 신청자들 목록 중 원하는 신청자의 신청을 수락 또는 거절이 가능하다.
        - 신청을 수락해 듀오가 매칭이 되면 신청자의 신고하기 , 칭찬하기를 누를 수 있다.
    - 상세 페이지에서 게시글 수정, 삭제가 가능하다.
        - 수정 버튼을 누르면 게시글 수정 페이지에서 수정이 가능하다.

### 신청자

- 듀오 게시판 내의 게시글 중에서 자신이 원하는 작성자(유저)의 글에 신청하기를 할 수 있다.
- 신청하기를 누르면 신청하기 버튼의 상태가 대기중 으로 바뀐다.
- 상대방이 수락할 시, 버튼이 매칭됨으로 바뀐다.
    - 수락하여 매칭이 되면 , 작성자의 신고하기 칭찬하기를 누를 수 있다.
 
듀오 게시판을 이용하여 매칭을 하고, 게임을 완료하면 상대방에 대하여 평가를 할 기회가 주어진다.

- 평가는 좋아요, 싫어요 를 선택하는 것으로 이루어진다.

좋아요를 누르면 매칭한 상대의 신뢰점수는 2점이 주어진다.

싫어요를 누르면 매칭한 상대의 신뢰점수는 -1점이 주어진다.



![image](https://github.com/MarkZiRo/lol/assets/37473857/33ebf77a-f143-429b-a3d2-7f75c2a57a9c)


