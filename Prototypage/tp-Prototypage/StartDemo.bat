rem Start RadarGL Demo 
rem
set IVYBUS=10.3.8.255:3456
set IVYBUS=127.255.255.255:666
rem cd V:\Cours\Prototypage -Jestin\tp Ivy\RadarGL
c:
cd "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\SDER PII\RadarGL"

cd .\Program Files\RadarGL
rem Start RadarGL config Radar
start /B RadarGL.exe -f ..\..\Xanthane -config GlanceConfig.xml -acc reims -wp WP1 -role TC -sectors UH
cd ..\..

rem Start RadarGL config Trajecto
rem start /B RadarGL.exe -f ..\..\Xanthane -c GlanceTrajecto.xml -acc reims -wp WP1 -role TC -sectors UH

cd .\Program Files\Horloge\
rem Start Horloge
start /B Horloge.exe -start
cd ..\..

cd .\Program Files\Rejeu\
rem Start Rejeu
rejeu.exe ..\..\Data\SWR700B.rej -s 07:00:00

