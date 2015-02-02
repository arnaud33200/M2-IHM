package  {
	
	import flash.display.MovieClip;
	
	
	public class BadGreenShooted extends MovieClip {
		
		
		public function BadGreenShooted(e:GameEvent) {
			// constructor code
			score.text = "" + e.door.score;
		}
	}
	
}
