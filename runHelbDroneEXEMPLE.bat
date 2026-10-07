@echo off
echo Compilation...
javac --module-path "C:PATH\JAVAFX\SDK\LIB" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web *.java

if %errorlevel% neq 0 (
    echo Erreur de compilation!
    pause
    exit /b %errorlevel%
)

echo Lancement du jeu...
java --module-path "C:\Users\akimb\Downloads\openjfx-17.0.17_windows-x64_bin-sdk\javafx-sdk-17.0.17\lib" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web Main

echo Nettoyage...
del /S /Q *.class 2>nul

pause
