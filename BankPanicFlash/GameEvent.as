package {
	
	/*override public function registerEvents():void
	{
		model.addEventListener( EventNames.SECTION_1, show );
		model.addEventListener( ( EventNames.SECTION_1 + Model.CLEAR ), hide );
	}
	*/
	
	public class GameEvent {

		public static const DOOR_IS_OPEN:String = "doorisopen";
		public static const SHOOT_PRESSED:String = "shootpressed";
		public static const DIRECTION_PRESSED:String = "directionpressed";
		public static const TOO_LATE:String = "toolate";
		public static const WRONG_TARGET:String = "wrongtarget";
	}
	
}
