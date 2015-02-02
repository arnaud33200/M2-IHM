package  {
	
	import flash.utils.Timer;
    import flash.events.TimerEvent;
	import flash.events.Event;
	
	public class DoorManager {

		public static var timePeriod = 200;
		
		private var doors:Array;
		private var doorWindow:Array;
		private var model:GameModel;
		
		private var newComingTimer:Timer;
		
		public function DoorManager(m:GameModel) {
			model = m;
			doors = new Array(12);
			for (var i:int=0; i<12; i++){
				doors[i] = new DoorModel(i, this);
				doors[i].addEventListener(DoorEvent.TOO_EARLY, too_early);
				doors[i].addEventListener(DoorEvent.TOO_LATE, too_late);
				doors[i].addEventListener(DoorEvent.GOOD_SHOOT, good_shoot);
				doors[i].addEventListener(DoorEvent.MONEY, money);
				doors[i].addEventListener(DoorEvent.WRONG_TARGET, wrong_target);
				doors[i].addEventListener(DoorEvent.CLOSING_END, closing_end);
			}
			doorWindow = [0,1,2];
			
			newComingTimer = new Timer(timePeriod, 0);
			newComingTimer.addEventListener(TimerEvent.TIMER, createNewComming);
			newComingTimer.start();
		}
		
		public function gamePause() {
			doorWindow = [0,1,2];
			newComingTimer.stop();
			for (var i:int=0; i<12; i++){
				doors[i].doorPause();
			}
		}
		
		public function gameResume() {
			newComingTimer.start();
		}
		
		public function getDoorWindow():Array {
			return doorWindow;
		}
		
		public function someOneComing(d:DoorModel) {
			model.someOneComing(d);
		}
		
	// after moving door window, check if one of the three doors
	// is open and then begin the action
		public function isAllDoorsClosed():Boolean {
			var rtn:Boolean = true;
			var msg:String = "";
			for (var i:int = 0; i < 3; i++) {
				var c:DoorModel = doors[doorWindow[i]];
				if (c.doorState != DoorModel.STATECLOSE && 
					c.doorState != DoorModel.STATECOMING) {
					rtn = false;
					msg += c.number + " - ";
				}
			}
			return rtn;
		}
		
		public function doorLogicNumber(n:int):int {
			for (var i:int=0; i<3; i++) {
				if (doors[doorWindow[i]].number == n) {
					return i;
				}
			}
			return 0;
		}
		
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// ~~~~~~~~ DOOR EVENT RECEPTION
// evenement recut par la porte, transition entre porte et model
		
		private function too_early(e:DoorEvent):void {
			trace("[" + e.door.number + "] too early !!!");
			e.door.score = 100;
			model.badShooted(e.door);
			model.doorClose(e);
		}
		private function good_shoot(e:DoorEvent):void {
			trace("[" + e.door.number + "] BOOM NICE SHOT !!!");
			model.badShooted(e.door);
			model.doorClose(e);
		}
		private function money(e:DoorEvent):void {
			model.moneyReceived(e.door.number);
			model.doorClose(e);
		}
		// bad action
		private function too_late(e:DoorEvent):void {
			trace("[" + e.door.number + "] TOO LATE !!!");
			model.doorClose(e);
			model.gameLooseTooLate(e);
		}
		private function wrong_target(e:DoorEvent):void {
			trace("[" + e.door.number + "] WRONG !!!");
			model.doorClose(e);
			model.gameLooseWrong(e);
		}
		private function closing_end(e:DoorEvent):void {
			model.doorClose(e);
		}
		
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		public function shootAtTheDoor(n:int):void {
			var door:DoorModel = doors[doorWindow[n]];
			if (door.doorState == DoorModel.STATEOPENBAD ||
				door.doorState == DoorModel.STATEACTIONBAD ||
				door.doorState == DoorModel.STATEOPENGOOD ||
				door.doorState == DoorModel.STATEACTIONGOOD) {
					//trace ("[" + n + "] - SHOOT");
					door.shootReceived();
				}
		}
		
		public function createNewComming(e:TimerEvent):void {
			var r = (Math.floor(Math.random() * (10 - 1 + 1)) + 1);
			if (r <= 7) {
				
			} else  {
				r = (Math.floor(Math.random() * (11 - 0 + 1)));
				var d:DoorModel = doors[r];
				if ( d.doorState == DoorModel.STATECLOSE) {
					doors[r].someoneComing();
					
					var msg:String = "COMING - ";
					for each (var door:DoorModel in doors) {
						if (door.doorState == DoorModel.STATECOMING)
							msg += "[" + door.number + "] -";
					}
					//trace(msg);
				}
			} 

		}
		
		public function moveDoorWindowLeft():void {
			doorWindow[0] = doorWindow[0] - 1;
			doorWindow[1] = doorWindow[1] - 1;
			doorWindow[2] = doorWindow[2] - 1;
			if (doorWindow[0] < 0) doorWindow[0] = 11;
			else if (doorWindow[0] > 11) doorWindow[0] = 0;
			if (doorWindow[1] < 0) doorWindow[1] = 11;
			else if (doorWindow[1] > 11) doorWindow[1] = 0;
			if (doorWindow[2] < 0) doorWindow[2] = 11;
			else if (doorWindow[2] > 11) doorWindow[2] = 0;
			trace("doorWindow [ " + doorWindow[0] + " - " + doorWindow[1] + " - " + doorWindow[2] + " ]");
		}
		
		public function moveDoorWindowRight():void {
			doorWindow[0] = doorWindow[0] + 1;
			doorWindow[1] = doorWindow[1] + 1;
			doorWindow[2] = doorWindow[2] + 1;
			if (doorWindow[0] < 0) doorWindow[0] = 11;
			else if (doorWindow[0] > 11) doorWindow[0] = 0;
			if (doorWindow[1] < 0) doorWindow[1] = 11;
			else if (doorWindow[1] > 11) doorWindow[1] = 0;
			if (doorWindow[2] < 0) doorWindow[2] = 11;
			else if (doorWindow[2] > 11) doorWindow[2] = 0;
			trace("doorWindow [ " + doorWindow[0] + " - " + doorWindow[1] + " - " + doorWindow[2] + " ]");
		}
		
		public function checkDoorReady():void {
			if (doors[doorWindow[0]].doorState == DoorModel.STATEREADY) {
				doors[doorWindow[0]].startDoorOpening()

			}
			if (doors[doorWindow[1]].doorState == DoorModel.STATEREADY) {
				doors[doorWindow[1]].startDoorOpening()

			}
			if (doors[doorWindow[2]].doorState == DoorModel.STATEREADY) {
				doors[doorWindow[2]].startDoorOpening()

			}
		}
		
		public function doorIsOpen(d:DoorModel):void {
			model.doorOpen(d);
		}
		
// Quand une porte vient de passé à l'état de prét
// on verifie que la porte est bien dans la fenetre
		public function doorIsReady(d:DoorModel):void {
			if (doorWindow[0] == d.number || 
				doorWindow[1] == d.number || 
				doorWindow[2] == d.number) {
					d.startDoorOpening();
				}
		}
		
		public function beginOpenDoor(d:DoorModel):void {
			model.doorBeginOpen(d);
		}

	}
	
}
