@ECHO OFF
@ECHO ---------------------------
@ECHO Iniciando MySQL 8.0.12 (x64)
@ECHO ---------------------------
@ECHO Para desligar o MySQL, basta fechar a janela.
cd /d %~dp0
.\bin\mysqld
pause