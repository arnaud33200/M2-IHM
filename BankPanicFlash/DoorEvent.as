package  {
	
	import flash.events.Event;
	
	public class DoorEvent extends Event {
		
		public static const TOO_EARLY:String = "tooearly";
		public static const TOO_LATE:String = "toolate";
		public static const GOOD_SHOOT:String = "goodshoot";
		public static const MONEY:String = "money";
		public static const WRONG_TARGET:String = "wrongtarget";
		public static const CLOSING_END:String = "closingend";

		
		public var door:DoorModel;
		public var score:int = 0;

		public function DoorEvent(type:String, d:DoorModel) {
			super(type, bubbles, cancelable);
			door = d;
		}

	}
	
}
