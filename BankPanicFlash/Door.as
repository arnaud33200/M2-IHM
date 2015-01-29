package  {
	
	import flash.display.MovieClip;
	import flash.utils.Timer;
    import flash.events.TimerEvent;
	
	public class Door extends MovieClip {
		
		private var p:MovieClip;
		private var kind:int;
		private var t:Timer;
		private var n:int;
		private var red:Boolean;
		
		public function Door(k:int, n:int) {
			red = false;
			kind = k;
			this.n = n;
			//t = new Timer(500, 1);
			//t.addEventListener(TimerEvent.TIMER, timerListener);
			//t.start();
			this.gotoAndPlay(1);
		}
		
		public function badShooted(e:GameEvent):void {
			removeChildren();
			if (red) p = new BadRedShooted(e);
			else p = new BadGreenShooted(e);
			addChild(p);
		}
		
		private function timerListener (e:TimerEvent):void{
			
		}
	}
	
}
