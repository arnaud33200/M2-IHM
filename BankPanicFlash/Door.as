package  {
	
	import flash.display.MovieClip;
	import flash.utils.Timer;
    import flash.events.TimerEvent;
	
	public class Door extends MovieClip {
		
		private var p:BadRed;
		private var t:Timer;
		
		public function Door() {
			t = new Timer(600, 1);
			t.addEventListener(TimerEvent.TIMER, timerListener);
			t.start();
		}
		
		private function timerListener (e:TimerEvent):void{
			p = new BadRed();
			p.y = -24;
			addChild(p);
		}
	}
	
}
