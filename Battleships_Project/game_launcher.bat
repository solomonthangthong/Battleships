SET LIBDIR=lib
SET SRCDIR=src
SET BINDIR=bin
SET BINERR=jap-javac.err
SET JARNAME=jap.jar
SET JAROUT=jap-jar.out
SET JARERR=jap-jar.err
SET DOCDIR=doc
SET DOCPACK=jap
SET DOCERR=jap-javadoc.err
SET MAINCLASSSRC=src/jap/GameBasic.java
SET MAINCLASSBIN=jap.GameBasic
SET IMAGES=images
SET RESOURCES=resources

@echo off

ECHO "0. Preconfiguring ................."
mkdir "%BINDIR%/%RESOURCES%"
xcopy "%RESOURCES%" "%BINDIR%/%RESOURCES%" /Y
mkdir "%BINDIR%/%IMAGES%"
xcopy "%IMAGES%" "%BINDIR%/%IMAGES%" /Y

ECHO "1. Compiling ......................"
::javac -Xlint -cp ".;src;/SOFT/copy/dev/java/javafx/lib/*;/SOFT/COPY/db/derby/lib/*" src/Lab.java -d bin 2> labs-javac.err
javac -Xlint -cp ".;%SRCDIR%" %MAINCLASSSRC% -d %BINDIR% 2> %BINERR%

:: ECHO "Running (outside JAR) ........................."
:: start java -cp ".;bin;/SOFT/copy/dev/java/javafx/lib/*" Battleship.Main

ECHO "2. Creating Jar ..................."
cd bin
::jar cvfe Battleship.jar Lab . > labs-jar.out 2> labs-jar.err
jar cvfe %JARNAME% %MAINCLASSBIN% . > ../%JAROUT% 2> ../%JARERR%

cd bin
ECHO "4. Running Jar ...................."
::start java --module-path "/SOFT/COPY/dev/LIBS/javafx/lib;/SOFT/COPY/db/derby/lib" --add-modules javafx.controls,javafx.fxml -jar Battleship.jar
start java --module-path "../%LIBDIR%" -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on
