timeout 2
echo %time% - Update started >> update.log
ROBOCOPY Update . /R:3 /W:3  /PURGE /XD dirs * /XF file %0 *.log >> update.log
timeout 2
echo %time% - Update finished. Exit code %errorlevel% >> update.log
start javaw -jar -Xms1024m -Xmx1024m Simburde.jar %errorlevel% >> update.log
exit /B