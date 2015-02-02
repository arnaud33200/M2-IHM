package  {
	import flash.display.Stage;
	import flash.utils.Timer;
    import flash.events.TimerEvent;
	import flash.events.EventDispatcher;

	public class GameModel extends EventDispatcher {
		
		public static const SHOOT1:int = 1;
		public static const SHOOT2:int = 2;
		public static const SHOOT3:int = 3;
		
		public static const GOLEFT:int = 4;
		public static const GORIGHT:int = 5;
		
		public static const STATEIDLE:int = 0;
		public static const STATETRANSITION:int = 1;
		public static const STATEACTION:int = 2;
		public static const STATEEND:int = 3;
		
		private var state:int;
		private var gameTime:Timer;
		private var wait:Timer;
		private var transition:Timer;
		private var doorOpened:int;
		
		public var goldCollected:Array;
		public var score:int;
		public var life:int;
		public var roundNumber:int;
		
		private var doorManager:DoorManager;
		
		public function GameModel() {
			score = 0;
			life = 3;
			roundNumber = 1;
			goldCollected = new Array(12);
			for (var i:int = 0; i<12; i++) {
				goldCollected[i] = 0;
			}
			doorManager = new DoorManager(this);
			doorOpened = 0;
			state=0;
			gameTime = new Timer(180000, 1);
			gameTime.addEventListener(TimerEvent.TIMER, timeIsUp);
			//gameTime.start();
			wait = new Timer(500,1);
			wait.addEventListener(TimerEvent.TIMER, waitFinished);
			transition = new Timer(800,1);
			transition.addEventListener(TimerEvent.TIMER, transitionFinished);
		}
		
		public function gamePause() {
			state = STATEEND;
			doorManager.gamePause();
		}
		
		public function GameResume() {
			state = STATEIDLE;
			dispatchEvent(new GameEvent(GameEvent.GAME_NEW_SCORE,0));
			doorManager.gameResume();
		}
		
// Remettre tout à zéro après un game
		public function GameReset() {
			state = STATEEND;
			for (var i:int=0; i<12; i++) {
				goldCollected[i] = 0;
			}
			doorManager.gamePause();
		}
		
		public function someOneComing(d:DoorModel) {
			var evt:GameEvent = new GameEvent(GameEvent.DOOR_COMING, d.number);
			evt.door = d;
			dispatchEvent(evt);
		}
		
		public function shootDoor(n:int):void {
			doorManager.shootAtTheDoor(n);
			dispatchEvent(new GameEvent(GameEvent.DOOR_SHOOTED, n));
			// tell the view that a shoot has to be animated !!!
		}
		
		public function changeDoorWindow(d:int):void {
			var evt:GameEvent;
			transition.start();
			if (d == GOLEFT) {
				doorManager.moveDoorWindowLeft();
				evt = new GameEvent(GameEvent.DOORS_MOVING_LEFT,0)
			} else {
				doorManager.moveDoorWindowRight();
				evt = new GameEvent(GameEvent.DOORS_MOVING_RIGHT,0)
			}
			evt.window = doorManager.getDoorWindow();
			dispatchEvent(evt);
		}
		
		public function moneyReceived(n:int):void {
			goldCollected[n]++;
			addScore(100);
			dispatchEvent(new GameEvent(GameEvent.DOOR_MONEY, n));
			checkForWin();
		}
		
// A chaque argent reçu, verifier qu'on a collecté toutes les portes
		public function checkForWin() {
			var count:int = 0;
			for (var i:int=0; i<12; i++) {
				if (goldCollected[i] > 0) {
					count++;
				}
			}
			if (count >= 12) {
				GameReset();
				roundNumber++;
				dispatchEvent(new GameEvent(GameEvent.ROUND_WIN, 0));
			}
		}
		
		public function addScore(s:int):void {
			score += s;
			dispatchEvent(new GameEvent(GameEvent.GAME_NEW_SCORE,0));
			
		}
		
		public function badShooted(d:DoorModel):void {
			addScore(d.score);
			var evt:GameEvent = new GameEvent(GameEvent.BAD_SHOOTED, 0);
			evt.numberOpen = doorManager.doorLogicNumber(d.number);
			evt.door = d;
			dispatchEvent(evt);
		}
		
		public function gameLooseWrong(e:DoorEvent):void {
			if (state == STATEEND) {
				return;
			}
			life--;
			gamePause();
			var evt:GameEvent = new GameEvent(GameEvent.GAMEOVER_WRONG, 0);
			dispatchEvent(evt);
		}
		
		public function gameLooseTooLate(e:DoorEvent):void {
			if (state == STATEEND) {
				return;
			}
			life--;
			gamePause();
			var evt:GameEvent = new GameEvent(GameEvent.GAMEOVER_TOO_LATE, 0);
			dispatchEvent(evt);
		}
		
		// when the user decide to choice 3 doors
		// check if a door is open
		private function waitFinished (e:TimerEvent):void{
			wait.stop();
			doorManager.checkDoorReady();
		}
		
		public function doorBeginOpen(d:DoorModel):void {
			var evtact:GameEvent = new GameEvent(GameEvent.DOOR_OPEN,0);
			evtact.numberOpen = doorManager.doorLogicNumber(d.number);
			evtact.door = d;
			dispatchEvent(evtact);
		}
		
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// MACHINE ETAT
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		private function timeIsUp (e:TimerEvent):void{
			switch(state) {
				case STATEIDLE:
					state = 3;
					trace ("TIME's UP");
					wait.stop();
					//gameLoose();
					break;
				case STATETRANSITION:
					state = 3;
					trace ("TIME's UP");
					//gameLoose();
					break;
				case STATEACTION:
					state = 3;
					trace ("TIME's UP");
					//gameLoose();
					break;
				case STATEEND:
					state = 3;
					trace ("TIME's UP");
					//gameLoose();
					break;
			}
		}
		
		private function transitionFinished(e:TimerEvent):void{
			transition.stop();
			switch(state) {
				case STATEIDLE:
					break;
				case STATETRANSITION:
					state = 0;
					wait.start();
					break;
				case STATEACTION:
					break;
				case STATEEND:
					break;
			}
		}
		
		public function ShootPressed(e:EventShootDoor):void {
			
			switch(state) {
				case STATEIDLE:
					state = 0;
					shootDoor(e.door);
					break;
				case STATETRANSITION:
					break;
				case STATEACTION:
					state = 2;
					shootDoor(e.door);
					break;
				case STATEEND:
					break;
			}
		}
		
		public function DirectionPressed(action:int):void {
			switch(state) {
				case STATEIDLE:
					state = 1;
					changeDoorWindow(action);
					wait.stop();
					break;
				case STATETRANSITION:
					state = 1;
					break;
				case STATEACTION:
					break;
				case STATEEND:
					break;
			}
		}
		
		public function doorOpen(d:DoorModel):void {
			
			switch(state) {
				case STATEIDLE:
					state = 2;
					doorOpened = 1;
					break;
				case STATETRANSITION:
					break;
				case STATEACTION:
					state = 2;
					doorOpened++;
					break;
				case STATEEND:
					break;
			}
		}
		
		public function doorClose(e:DoorEvent):void {
			switch(state) {
				case STATEIDLE:
					break;
				case STATETRANSITION:
					break;
				case STATEACTION:
					doorOpened--;
					if (doorManager.isAllDoorsClosed()) {
						state = 0;
					} else {
						state = 2;
					}
					break;
				case STATEEND:
					break;
			}
		}
	}
	
}
