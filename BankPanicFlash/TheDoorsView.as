package  {
	
	import flash.display.MovieClip;
	
	
	public class TheDoorsView extends MovieClip {
		
		private var model:GameModel;
		
		private var mr:MovingDoorsRight; 
		private var ml:MovingDoorsLeft;
		private var sd:StaticDoors;
		
		public function TheDoorsView(m:GameModel) {
			model = m;
			model.addEventListener(GameEvent.DOORS_MOVING_LEFT, moveLeft);
			model.addEventListener(GameEvent.DOORS_MOVING_RIGHT, moveRight);
			model.addEventListener(GameEvent.DOOR_OPEN, doorOpen);
			sd = new StaticDoors(5,9,8);
			addChild(sd);
		}
		
		function doorOpen(e:GameEvent):void {
			var d:Door = new Door(e.door.person, e.numberOpen);
			addChild(d);
		}
		
		function moveLeft(e:GameEvent):void {
			removeChildren();
			mr = new MovingDoorsRight();
			addChild(mr);
		}

		function moveRight(e:GameEvent):void {
			removeChildren();
			ml = new MovingDoorsLeft();
			addChild(ml);
			
		}
	}
	
}
