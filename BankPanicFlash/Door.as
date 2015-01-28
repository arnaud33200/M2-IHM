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
			t = new Timer(500, 1);
			t.addEventListener(TimerEvent.TIMER, timerListener);
			t.start();
		}
		
		public function badShooted(e:GameEvent):void {
			if (red) p = new BadRedShooted(e);
			else p = new BadGreenShooted(e);
			addChild(p);
		}
		
		private function timerListener (e:TimerEvent):void{
			var r:int;
			if (kind == DoorModel.PERSONBAD) {
				r = (Math.floor(Math.random() * (1 - 0 + 1)) + 0);
				if (r == 0) { p = new BadRed(); red = true; }
				else p = new BadGreen();
			} else {
				r = (Math.floor(Math.random() * (1 - 0 + 1)) + 0);
				if (r == 0) p = new GoodRed();
				else p = new GoodGreen();
			}
			this.y = 457;
			this.x -= 20;
			switch (n) {
				case 0: this.x = 122; break;
				case 1: this.x = 373; break;
				case 2: this.x = 624; break;
			}
			
			p.y = -24;
			addChild(p);
		}
	}
	
}
