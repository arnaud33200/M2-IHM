package  {
	
	import flash.display.MovieClip;
	
	
	public class shoot extends MovieClip {
		
		
		public function shoot(n:int) {
			switch (n) {
				case 0: this.x = 200; break;
				case 1: this.x = 500; break;
				case 2: this.x = 700; break;
			}
			this.y = 400
		}
	}
	
}
