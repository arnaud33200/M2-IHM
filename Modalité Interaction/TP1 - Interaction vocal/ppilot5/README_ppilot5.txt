utiliser ppilot5
----------------

ppilot permet d�utiliser des syst�mes de synth�se vocale compatibles SAPI5.

Lancement
ppilot5 -b 127.255.255.255:2010 -r Virginie -o "ScanSoft Virginie_Dri40_16kHz"

  -b adresse IP + port
  -r nom sous lequel appara�tra l�agent sous ivy
  -o nom du moteur de synth�se utilis�

Commandes
  * Synth�se
    - ppilot Say="hello" ppilot prononce "hello" et envoie ppilot Answer=Finished quand le buffer est vide

  * Commandes
    - ppilot Command=Stop la synth�se vocale devrait �tre stopp�e.
    - ppilot Command=Pause la synth�se vocale est mise en pause. ppilot renvoie ppilot Answer=Paused
    - ppilot Command=Resume la synth�se vocale est relanc�e si elle �tait en pause pr�c�demment. ppilot renvoie ppilot Answer=Resumed
    - ppilot Command=Quit l�application se ferme

  * Param�tres
    - ppilot Param=Pitch:value le pitch devrait �tre chang� par la valeur donn�e. (ne fonctionne pas !)
    - ppilot Param=Speed:value la vitesse est chang�e par la valeur donn�e. ppilot renvoie ppilot Answer=SpeedValueSet:value
    - ppilot Param=Volume:value le volume est chang� par la valeur donn�e. ppilot renvoie ppilot Answer=VolumeValueSet:value

    