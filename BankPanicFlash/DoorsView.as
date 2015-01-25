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
		
		private var sp:shoot;
		
		public function DoorsView(m:GameModel) {
			model = m;
			model.addEventListener(GameEvent.DOORS_MOVING_LEFT, moveLeft);
			model.addEventListener(GameEvent.DOORS_MOVING_RIGHT, moveRight);
			model.addEventListener(GameEvent.DOOR_OPEN, doorOpen);
			model.addEventListener(GameEvent.DOOR_SHOOTED, doorShooted);
			sd = new StaticDoors(5,9,8);
			addChild(sd);
			
			n1 = new TextField();
		}
		
		function doorShooted(e:GameEvent):void {
			var i:int = e.numberOpen;
			sp = new shoot(i);
			addChild(sp);
		}
		
		function doorOpen(e:GameEvent):void {
			var m:Door = new Door(e.door.person, e.numberOpen);
			m.gotoAndPlay(1);
			this.gotoAndPlay(1);
			addChild(m);
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
