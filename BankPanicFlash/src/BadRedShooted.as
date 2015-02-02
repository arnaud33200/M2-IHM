package  {
	
	import flash.display.MovieClip;
	import flash.ui.GameInput;
	
	
	public class BadRedShooted extends MovieClip {
		
		
		public function BadRedShooted(e:GameEvent) {
			// constructor code
			score.text = "" + e.door.score;
		}
	}
	
}
