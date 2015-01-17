package {
	
	import flash.events.*;
	
	public class EventShootDoor extends Event {
		
		public static const SHOOT_EVENT:String = "shootevent";

		public var door:int;

		public function EventShootDoor(type:String, d:int) {
			super(type, bubbles, cancelable);
			door = d;
		}


	}
	
}
