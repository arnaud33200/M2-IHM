package  {
	
	import flash.display.MovieClip;
	
	
	public class ScoreView extends MovieClip {
		
		private var model:GameModel;
		
		public function ScoreView(m:GameModel) {
			model = m;
			model.addEventListener(GameEvent.GAME_NEW_SCORE, newscore);
		}
		
		private function newscore(evt:GameEvent):void {
			scoreText.text = "" + model.score;
		}
	}
	
}
