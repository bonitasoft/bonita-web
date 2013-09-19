@echo off
set WORKSPACEPATH=C:\Users\Fabio\maintenance
set PATHTOPROJECT=%WORKSPACEPATH%\bonita-web
set MAVENREPOPATH=C:\Users\Fabio\.m2\repository
set BONITA_HOME="C:\bonita-60x"
call %PATHTOPROJECT%\console\console-war\common.cmd
pause