@echo off
set WORKSPACEPATH=C:\BosWorkspace
set PATHTOPROJECT=%WORKSPACEPATH%\bos-web
set MAVENREPOPATH=C:\Users\bonitasoft\.m2\repository
set BONITA_HOME="C:\BOSbuilds\BONITA_HOME_6.0"
call %PATHTOPROJECT%\console\console-war\common.cmd
pause