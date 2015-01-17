package  {
	
	import flash.display.MovieClip;
	import flash.text.TextField;
	
	public class DoorsView extends MovieClip {

		private var model:GameModel;
		
		private var mr:MovingDoorsRight; 
		private var ml:MovingDoorsLeft;
		private var sd:StaticDoors;
		
		private var n1:TextField;
		private var n2:TextField;
		private var n3:TextField;
		
		public function DoorsView(m:GameModel) {
			model = m;
			model.addEventListener(GameEvent.DOORS_MOVING_LEFT, moveLeft);
			model.addEventListener(GameEvent.DOORS_MOVING_RIGHT, moveRight);
			model.addEventListener(GameEvent.DOOR_OPEN, doorOpen);
			sd = new StaticDoors();
			addChild(sd);
			
			n1 = new TextField();
		}
		
		function doorOpen(e:GameEvent):void {
			var m:Door = new Door(e.door.person, e.numberOpen);
			addChild(m);
			this.play();
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
