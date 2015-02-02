package  {
	
	import flash.display.MovieClip;
	import flash.text.TextField;
	
	public class DoorsView extends MovieClip {

		private var model:GameModel;
		
		private var mr:MovingDoorsRight; 
		private var ml:MovingDoorsLeft;
		private var sd:StaticDoors;
		
		private var doors:Array;
		
		private var sp:shoot;
		
		public function DoorsView(m:GameModel) {
			doors = new Array(3);
			model = m;
			model.addEventListener(GameEvent.DOORS_MOVING_LEFT, moveLeft);
			model.addEventListener(GameEvent.DOORS_MOVING_RIGHT, moveRight);
			model.addEventListener(GameEvent.DOOR_OPEN, doorOpen);
			model.addEventListener(GameEvent.DOOR_SHOOTED, doorShooted);
			model.addEventListener(GameEvent.BAD_SHOOTED, badShooted);
			sd = new StaticDoors(5,9,8);
			addChild(sd);
			
			//n1 = new TextField();
		}
		
				
		function badShooted(e:GameEvent):void {
			var d:Door = doors[e.numberOpen];
			d.badShooted(e);
		}
		
		function doorShooted(e:GameEvent):void {
			var i:int = e.numberOpen;
			sp = new shoot(i);
			addChild(sp);
		}
		
		function doorOpen(e:GameEvent):void {
			trace("---->");
			doors[e.numberOpen] = new Door(e.door.person, e.numberOpen);
			doors[e.numberOpen].gotoAndPlay(1);
			addChild(doors[e.numberOpen]);
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
