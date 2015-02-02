package  {
	
	import flash.display.MovieClip;
	
	
	public class shoot extends MovieClip {
		
		
		public function shoot(n:int = -1) {
			switch (n) {
				case 0: this.x = 112; break;
				case 1: this.x = 363; break;
				case 2: this.x = 614; break;
			}
			this.y = 304
		}

	}
	
}
