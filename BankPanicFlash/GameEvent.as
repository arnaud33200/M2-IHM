package {

	import flash.events.Event;
	
	public class GameEvent extends Event {

		public static const DOORS_MOVING_LEFT:String = "doorsmovingleft";
		public static const DOORS_MOVING_RIGHT:String = "doorsmovingright";
		public static const DOOR_OPEN:String = "dooropen";
		
		public var numberOpen:int;
		public var door:DoorModel;

		public function GameEvent(type:String) {
			super(type, bubbles, cancelable);
			numberOpen = -1;
		}

	}
	
}
