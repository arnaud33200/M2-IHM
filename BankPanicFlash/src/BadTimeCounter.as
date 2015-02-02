package  {
	
	import flash.display.MovieClip;
	import flash.utils.Timer;
    import flash.events.TimerEvent;
	
	public class BadTimeCounter extends MovieClip {
		
		private var chronoCount;
		private var t:Timer;
		
		public function BadTimeCounter(model:GameModel) {
			model.addEventListener(GameEvent.BAD_SHOOTED, shooted);
			chronoCount = 0;
			t = new Timer(40,0);
			t.addEventListener(TimerEvent.TIMER, time);
			t.start();
		}
		
		private function shooted(e:GameEvent) {
			stopTimer();
		}
		
		private function time(evt:TimerEvent):void {
			chronoCount++;
			var str:String = "";
			if (chronoCount < 10) { str += "0"; }
			str += chronoCount;
			chronoText.text = "00:" + str;
		}
		
		public function stopTimer():void {
			t.stop();
		}
	}
	
}
