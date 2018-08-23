[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Korean](https://img.shields.io/badge/language-Korean-blue.svg)](#korean)
[![Englsh](https://img.shields.io/badge/language-English-orange.svg)](#english)


<a name="korean"></a>
ValidatorBatch-v0.1 (공간자료 검증 배치파일 v0.1) 
=======
이 프로젝트는 국토공간정보연구사업 중 [공간정보 SW 활용을 위한 오픈소스 가공기술 개발]과제의 5차년도 연구성과 입니다.<br>
정식 버전은 차후에 통합된 환경에서 제공될 예정입니다.<br>
이 프로그램들은 완성되지 않았으며, 최종 완료 전 까지 문제가 발생할 수도 있습니다.<br>
발생된 문제는 최종 사용자에게 있으며, 완료가 된다면 제시된 라이선스 및 규약을 적용할 예정입니다.<br>

감사합니다.<br>
공간정보기술(주) 연구소 <link>http://www.git.co.kr/<br>
OpenGeoDT 팀


특징
=====
- QGIS 플러그인에서 Java 클래스를 외부에서 호출이 가능하게 Batch 파일로 연동합니다. 
- 검수 내용은 [공간자료 검증도구v1.0](https://github.com/ODTBuilder/Validator-v1.0) 에 의거합니다.


파라미터
=====
```css
*  --basedir       : 최상위 폴더
*  --filetype      : 파일타입
                     dxf
                     ngi
                     shp
*  --cidx          : 옵션타입 
                     1 - 수치지도  1.0
                     2 - 수치지도  2.0
                     3 - 지하시설물 1.0
                     4 - 지하시설물 2.0
                     5 - 임상도
*  --layerdefpath  : 레이어 정의 옵션 경로
*  --valoptpath    : 검수 옵션 경로
*  --objfilepath   : 검수 대상파일 경로
*  --crs           : 좌표계
```

프로세스구조
====

<img width="710" alt="2018-08-23 1 25 12" src="https://user-images.githubusercontent.com/13480171/44504711-2e9d2f00-a6d8-11e8-89fc-6371d15ae403.png">

연구기관
=====
- 세부 책임 : 부산대학교 <link>http://www.pusan.ac.kr/<br>
- 연구 책임 : 국토연구원 <link>http://www.krihs.re.kr/


Getting Started
=====
### 1. 환경 ###
- Java - OpenGDK 1.8.0.111 64 bit


Mail
=====
Developer : SG.LEE
ghre55@git.co.kr



-----
<a name="english"></a>
Validator-v1.0 (Validation tool for geospatial data v1.0) 
=======

This project is still a work in progress at its 4th year. 
The final version of the project will be integrated in the later years.
The program may cause some problems since it is not complete.
The user is responsible to the caused problem and once it is complete, a new set of legal codes will be applied.

Thank you.
Geospatial Information Technology R&D <link>http://www.git.co.kr/<br>
Team OpenGeoDT


Characteristics
=======

-Validation tool for geospatial data v1.0 is a GIS integrated solution for inspecting and editing geospatial data based on GeoDT 2.2
-Provides tools for inspecting the geometric/logical structure of geospatial data.
-Supports various types of web browsers without installations of any plugins or ActiveX. 
-Can be customized or extended depending on the needs of the user since the program is written in JavaScript and Java.
-Follows the standards of OGC and provides more than 20 validating functions that follow the Korean law for making maps.


Getting Started
=====
### 1. Background ###
- Java - OpenGDK 1.8.0.111 64 bit

Mail
=====
Developer : SG.LEE
ghre55@git.co.kr
