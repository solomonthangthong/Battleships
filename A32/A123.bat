:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: SCRIPT CST8221 - Summer 2023
:: ---------------------------------------------------------------------
:: Begin of Script (A13 - S23)
:: ---------------------------------------------------------------------

CLS

:: LOCAL VARIABLES ....................................................

SET LIBDIR=lib
SET SRCDIR=src
SET BINDIR=bin
SET BINERR=jap-javac.err
SET JARNAME=jap.jar
SET CLIENTNAME=jap2.jar
SET JARSERVER=server.jar
SET JAROUT=jap-jar.out
SET JARERR=jap-jar.err
SET DOCDIR=doc
SET DOCPACK=jap
SET DOCERR=jap-javadoc.err
SET MAINCLASSSRC=src/jap/GameBasic.java
SET MAINCLASSBIN=jap.GameBasic
SET MAINCLASSSRC1=src/jap/GameBasic.java
SET MAINCLASSBIN1=jap.GameBasic
SET SERVERSRC=src/jap/Server.java
SET SERVERBIN=jap.Server
SET IMAGES=images
SET RESOURCES=resources

@echo off

ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @        S U M M E R - 2 0 2 3       @  @"
ECHO "@          ###    ##        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@        ###    ##                                                  @"
ECHO "@        ##    ###                 ###                              @"
ECHO "@         ##    ###                ###                              @"
ECHO "@           ##    ##               ###   #####  ##     ##  #####    @"
ECHO "@         (     (      ((((()      ###       ## ###   ###      ##   @"
ECHO "@     ((((     ((((((((     ()     ###   ######  ###  ##   ######   @"
ECHO "@        ((                ()      ###  ##   ##   ## ##   ##   ##   @"
ECHO "@         ((((((((((( ((()         ###   ######    ###     ######   @"
ECHO "@         ((         ((           ###                               @"
ECHO "@          (((((((((((                                              @"
ECHO "@   (((                      ((        /-----------------------\    @"
ECHO "@    ((((((((((((((((((((() ))         |  B A T T L E S H I P  |    @"
ECHO "@         ((((((((((((((((()           \-----------------------/    @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

ECHO "[LABS SCRIPT ---------------------]"


ECHO "0. Preconfiguring ................."
mkdir "%BINDIR%/%RESOURCES%"
xcopy "%RESOURCES%" "%BINDIR%/%RESOURCES%" /Y
mkdir "%BINDIR%/%IMAGES%"
xcopy "%IMAGES%" "%BINDIR%/%IMAGES%" /Y

ECHO "1. Compiling ......................"
::javac -Xlint -cp ".;src;/SOFT/copy/dev/java/javafx/lib/*;/SOFT/COPY/db/derby/lib/*" src/Lab.java -d bin 2> labs-javac.err
javac -Xlint -cp ".;%SRCDIR%" %MAINCLASSSRC% -d %BINDIR% 2> %BINERR%
javac -Xlint -cp ".;%SRCDIR%" %MAINCLASSSRC1% -d %BINDIR% 2> %BINERR%
ECHO "2. Compiling Server ......................"
javac -Xlint -cp ".;%SRCDIR%" %SERVERSRC% -d %BINDIR% 2> %BINERR%

:: ECHO "Running (outside JAR) ........................."
:: start java -cp ".;bin;/SOFT/copy/dev/java/javafx/lib/*" CST8221.Main

ECHO "3. Creating Jar ..................."
cd bin
::jar cvfe CST8221.jar Lab . > labs-jar.out 2> labs-jar.err
jar cvfe %JARNAME% %MAINCLASSBIN% . > ../%JAROUT% 2> ../%JARERR%
jar cvfe %CLIENTNAME% %MAINCLASSBIN1% . > ../%JAROUT% 2> ../%JARERR%
ECHO "3. Creating Jar for Server ..................."
jar cvfe %JARSERVER% %SERVERBIN% . > ../%JAROUT% 2> ../%JARERR%

ECHO "4. Creating Javadoc ..............."
cd ..
::javadoc -cp ".;bin;/SOFT/copy/dev/java/javafx/lib/*;/SOFT/COPY/db/derby/lib/*;/SOFT/COPY/dev/LIBS/jar/javax.servlet.jar" --module-path "C:\SOFT\COPY\dev\LIBS\javafx\lib" --add-modules javafx.controls -d doc -sourcepath src -subpackages CST8221 2> labs-javadoc.err
javadoc -cp ".;%BINDIR%;../%LIBDIR%" --module-path "%LIBDIR%" -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% 2> %DOCERR%

cd bin
ECHO "5. Running Jar ...................."
::start java --module-path "/SOFT/COPY/dev/LIBS/javafx/lib;/SOFT/COPY/db/derby/lib" --add-modules javafx.controls,javafx.fxml -jar CST8221.jar
start java --module-path "../%LIBDIR%" -jar %JARNAME%
start java --module-path "../%LIBDIR%" -jar %CLIENTNAME%
start java --module-path "../%LIBDIR%" -jar %JARSERVER%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (A13 - S23)
:: ---------------------------------------------------------------------
