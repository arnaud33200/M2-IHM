package  {
	
	import flash.events.*;
 	import flash.events.KeyboardEvent;
	import flash.display.Stage;
	import flash.events.EventDispatcher;
	
	public class PlayerController {
		
		private var stage:Stage;
		private var model:GameModel;
		private var scene:Object;
		private var dispatcher:EventDispatcher;
		
		private var shoot1Pressed:Boolean;
		private var shoot2Pressed:Boolean;
		private var shoot3Pressed:Boolean;
		private var leftPressed:Boolean;
		private var rightPressed:Boolean;

		public function PlayerController(stage:Stage, model:GameModel, scene:Object) {
			dispatcher = new EventDispatcher();
			
			this.stage = stage;
			this.model = model;
			this.scene = scene;
			
			shoot1Pressed = false;
			shoot2Pressed = false;
			shoot3Pressed = false;
			leftPressed = false;
			rightPressed = false;
			
			stage.addEventListener(KeyboardEvent.KEY_DOWN, fl_KeyboardDownHandler);
			stage.addEventListener(KeyboardEvent.KEY_UP, fl_KeyboardUpHandler);
			
		}
		
		function fl_KeyboardDownHandler(event:KeyboardEvent):void
		{
			//trace("Key Code Pressed: " + event.keyCode);
			if (event.keyCode == 87) {
				shoot1Pressed = true;
				model.ShootPressed( new EventShootDoor(EventShootDoor.SHOOT_EVENT,1) );
			}
			else if (event.keyCode == 88) {
				shoot2Pressed = true;
				model.ShootPressed( new EventShootDoor(EventShootDoor.SHOOT_EVENT,2) );
			}
			else if (event.keyCode == 40) {
				shoot3Pressed = true;
				model.ShootPressed( new EventShootDoor(EventShootDoor.SHOOT_EVENT,3) );
			}
			else if (event.keyCode == 37) {
				leftPressed = true;
				model.DirectionPressed(GameModel.GOLEFT);
			}
			else if (event.keyCode == 39) {
				rightPressed = true;
				model.DirectionPressed(GameModel.GORIGHT);
			}
			if (leftPressed || rightPressed) {
				scene.addEventListener(Event.ENTER_FRAME, fl_EnterFrameHandler_2);
			}
		}
		
		function fl_KeyboardUpHandler(event:KeyboardEvent):void
		{
			//trace("Key Code Pressed: " + event.keyCode);
			if (event.keyCode == 87) {
				shoot1Pressed = false;
			}
			else if (event.keyCode == 88) {
				shoot2Pressed = false;
			}
			else if (event.keyCode == 40) {
				shoot3Pressed = false;
			}
			else if (event.keyCode == 37) {
				leftPressed = false;
			}
			else if (event.keyCode == 39) {
				rightPressed = false;
			}
			if (!leftPressed && !rightPressed) {
				scene.removeEventListener(Event.ENTER_FRAME, fl_EnterFrameHandler_2);
			}
		}
		
		function fl_EnterFrameHandler_2(event:Event):void
		{
			if (leftPressed) {
				model.DirectionPressed(GameModel.GOLEFT);
			}
			if (rightPressed) {
				model.DirectionPressed(GameModel.GORIGHT);
			}
		}

	}
	
}
