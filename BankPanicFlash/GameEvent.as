package {

	import flash.events.Event;
	
	public class GameEvent extends Event {

		public static const DOORS_MOVING_LEFT:String = "doorsmovingleft";
		public static const DOORS_MOVING_RIGHT:String = "doorsmovingright";
		public static const DOOR_OPEN:String = "dooropen";
		
		public static const DOOR_SHOOTED:String = "doorshooted";
		public static const DOOR_MONEY:String = "doormoney";
		public static const DOOR_COMING:String = "doorcoming";
		
		public static const BAD_SHOOTED:String = "badshooted";
		
		public static const GAMEOVER_WRONG:String = "gameoverwrong";
		public static const GAMEOVER_TOO_LATE:String = "gameovertoolate";
		public static const GAMEOVER_TIME:String = "gameovertime";
		
		public static const ROUND_WIN:String = "roundwin";
		
		public var numberOpen:int;
		public var door:DoorModel;
		public var window:Array;
		
		public function GameEvent(type:String, n:int) {
			super(type, bubbles, cancelable);
			numberOpen = n;
		}

	}
	
}
