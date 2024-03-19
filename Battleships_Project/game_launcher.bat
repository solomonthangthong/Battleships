SET LIBDIR=lib
SET SRCDIR=src
SET BINDIR=bin
SET JARNAME=jap.jar
SET MAINCLASSSRC=src/jap/GameBasic.java
SET MAINCLASSBIN=jap.GameBasic
SET IMAGES=images
SET RESOURCES=resources

@echo off

ECHO "Preconfiguring"
mkdir "%BINDIR%/%RESOURCES%"
xcopy "%RESOURCES%" "%BINDIR%/%RESOURCES%" /Y
mkdir "%BINDIR%/%IMAGES%"
xcopy "%IMAGES%" "%BINDIR%/%IMAGES%" /Y

ECHO "Compiling"
::javac -Xlint -cp ".;src;/SOFT/copy/dev/java/javafx/lib/*;/SOFT/COPY/db/derby/lib/*" src/Project.java -d bin 2> project-javac.err
javac -Xlint -cp ".;%SRCDIR%" %MAINCLASSSRC% -d %BINDIR%

:: ECHO "Running (outside JAR) ........................."
:: start java -cp ".;bin;/SOFT/copy/dev/java/javafx/lib/*" Battleship.Main

ECHO "Creating Jar"
cd bin
jar cvfe %JARNAME% %MAINCLASSBIN% 

cd bin
ECHO "4. Running Jar ...................."
start java --module-path "../%LIBDIR%" -jar %JARNAME%
cd ..


