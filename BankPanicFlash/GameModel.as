package  {
	import flash.display.Stage;
	import flash.utils.Timer;
    import flash.events.TimerEvent;
	import flash.events.EventDispatcher;
	
	//dispatchEvent( new Event( event ) );
	
	public class GameModel extends EventDispatcher {
		
		public static const SHOOT1:int = 1;
		public static const SHOOT2:int = 2;
		public static const SHOOT3:int = 3;
		
		public static const GOLEFT:int = 4;
		public static const GORIGHT:int = 5;
		
		// 0:Idle - 1:Transition - 2:ActionTime - 3:loose - 4:win
		// il n'y pas pas d'implementation pour le type enum
		private var state:int;
		private var gameTime:Timer;
		private var wait:Timer;
		private var transition:Timer;
		private var doorOpened:int;
		
		private var doorManager:DoorManager;
		
		public function GameModel() {
			
			doorManager = new DoorManager(this);
			doorOpened = 0;
			state=0;
			gameTime = new Timer(60000, 1);
			gameTime.addEventListener(TimerEvent.TIMER, timeIsUp);
			gameTime.start();
			wait = new Timer(200,1);
			wait.addEventListener(TimerEvent.TIMER, waitFinished);
			transition = new Timer(800,1);
			transition.addEventListener(TimerEvent.TIMER, transitionFinished);
		}
		
		public function shootDoor(n:int):void {
			doorManager.shootAtTheDoor(n);
			// tell the view that a shoot has to be animated !!!
		}
		
		public function changeDoorWindow(d:int):void {
			transition.start();
			if (d == GOLEFT) {
				doorManager.moveDoorWindowLeft();
				dispatchEvent(new GameEvent(GameEvent.DOORS_MOVING_LEFT));
			} else {
				doorManager.moveDoorWindowRight();
				dispatchEvent(new GameEvent(GameEvent.DOORS_MOVING_RIGHT));
			}
		}
		
		public function gameWin(e:DoorEvent):void {
			
		}
		
		public function gameLoose(e:DoorEvent):void {
			trace("GAME LOOSE");
			doorClose(e);
		}
		
		// when the user decide to choice 3 doors
		// check if a door is open
		private function waitFinished (e:TimerEvent):void{
			wait.stop();
			doorManager.checkDoorReady();
		}
		
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		private function timeIsUp (e:TimerEvent):void{
			switch(state) {
				case 0:
					state = 3;
					wait.stop();
					//gameLoose();
					break;
				case 1:
					state = 3;
					//gameLoose();
					break;
				case 2:
					state = 3;
					//gameLoose();
					break;
				case 3:
					state = 3;
					//gameLoose();
					break;
				case 4:
					break;
			}
		}
		
		private function transitionFinished(e:TimerEvent):void{
			transition.stop();
			switch(state) {
				case 0:
					break;
				case 1:
					state = 0;
					wait.start();
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
			}
		}
		
		public function ShootPressed(e:EventShootDoor):void {
			
			switch(state) {
				case 0:
					state = 0;
					shootDoor(e.door);
					break;
				case 1:
					break;
				case 2:
					state = 2;
					shootDoor(e.door);
					break;
				case 3:
					break;
				case 4:
					break;
			}
		}
		
		public function DirectionPressed(action:int):void {
			switch(state) {
				case 0:
					state = 1;
					changeDoorWindow(action);
					wait.stop();
					break;
				case 1:
					state = 1;
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
			}
		}
		
		public function doorOpen(d:DoorModel):void {
			switch(state) {
				case 0:
					state = 2;
					doorOpened = 0;
					break;
				case 1:
					break;
				case 2:
					state = 2;
					doorOpened++;
					break;
				case 3:
					break;
				case 4:
					break;
			}
			var e:GameEvent = new GameEvent(GameEvent.DOOR_OPEN);
			e.numberOpen = doorManager.doorLogicNumber(d.number);
			e.door = d;
			dispatchEvent(e);
		}
		
		public function doorClose(e:DoorEvent):void {
			switch(state) {
				case 0:
					break;
				case 1:
					break;
				case 2:
					doorOpened--;
					if (doorOpened > 0) {
						state = 2;
					} else {
						state = 0;
					}
					
					break;
				case 3:
					break;
				case 4:
					break;
			}
		}
	}
	
}
