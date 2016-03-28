# PomoCronometer
Biblioteca desenvolvida para fazer o controle de um cronometro pomodoro para aplicativos Android.

Aplicativo que utiliza a biblioteca para controle do cronometro: [Google Play] (https://play.google.com/store/apps/details?id=com.vctapps.pomo)

### Exemplo
```
Pomodoro pomodoro = new Pomodoro(context, clock, onChangeTime);
pomodoro.start();
```
Parametros
context - Context

clock - TextView

onChangeTime - OnChangeTime (Interface callback)

### Métodos

####pomodoro.start()
  Inicia o cronometro
  
  return void
  
####pomodoro.stop()
  Para o cronometro
  
  return void
  
####pomodoro.isStarted()
  Método para verificar se o cronometro está rodando ou não
  
  return boolean - true: iniciado | false: parado
  
####pomodoro.onPause()
  Método que deve ser implementado no onPause da activity/fragment
  
  return void
  
####pomodoro.onResume()
  Método que deve ser implementado no onResume da activity/fragment
  
  return void
  
####pomodoro.onStop()
  Método que deve ser implementado no onStop da activity/fragment
  
  return void
  
####OnChangeTime
  Interface callback que deve ser passado como parametro na criação. Nela existe os métodos para implementações pessoais da app quando há troca de 
  ciclos. Por exemplo: ao começar um ciclo pomo, o callback é chamado para disparar uma notificação.
  
