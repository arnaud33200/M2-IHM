package  {
	
	import flash.utils.Timer;
    import flash.events.TimerEvent;
	import flash.events.Event;
	
	public class DoorManager {

		public static var timePeriod = 500;
		
		private var doors:Array;
		private var doorWindow:Array;
		private var model:GameModel;
		
		private var newComingTimer:Timer;
		
		public function DoorManager(m:GameModel) {
			model = m;
			doors = new Array(12);
			for (var i:int=0; i<12; i++){
				var d:DoorModel = new DoorModel(i, this);
				d.addEventListener(DoorEvent.TOO_EARLY, too_early);
				d.addEventListener(DoorEvent.TOO_LATE, too_late);
				d.addEventListener(DoorEvent.GOOD_SHOOT, good_shoot);
				d.addEventListener(DoorEvent.MONEY, money);
				d.addEventListener(DoorEvent.WRONG_TARGET, wrong_target);
				doors[i] = d;
			}
			doorWindow = [0,1,2];
			
			newComingTimer = new Timer(timePeriod, 0);
			newComingTimer.addEventListener(TimerEvent.TIMER, createNewComming);
			newComingTimer.start();
		}
		
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// ~~~~~~~~ DOOR EVENT RECEPTION
		
		private function too_early(e:DoorEvent):void {
			model.doorClose(e);
		}
		private function too_late(e:DoorEvent):void {
			trace("!!! TOO LATE !!!");
			model.doorClose(e);
		}
		private function good_shoot(e:DoorEvent):void {
			model.doorClose(e);
		}
		private function money(e:DoorEvent):void {
			trace("!!! MONEY !!!");
			model.doorClose(e);
		}
		private function wrong_target(e:DoorEvent):void {
			model.doorClose(e);
		}
		
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		public function shootAtTheDoor(n:int):void {
			
		}
		
		public function createNewComming(e:TimerEvent):void {
			var r = (Math.floor(Math.random() * (10 - 1 + 1)) + 1);
			if (r <= 7) {
				
			} else  {
				r = (Math.floor(Math.random() * (11 - 0 + 1)));
				var d:DoorModel = doors[r];
				if ( d.state == DoorModel.STATECLOSE) {
					doors[r].someoneComing();
					trace("[" + r + "] <<<< coming");
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
			trace(".... CHECK if ready");
			if (doors[doorWindow[0]].state == DoorModel.STATEREADY) {
				doors[doorWindow[0]].startDoorOpening()
				model.doorOpen(doors[doorWindow[0]]);
			}
			if (doors[doorWindow[1]].state == DoorModel.STATEREADY) {
				doors[doorWindow[1]].startDoorOpening()
				model.doorOpen(doors[doorWindow[1]]);
			}
			if (doors[doorWindow[2]].state == DoorModel.STATEREADY) {
				doors[doorWindow[2]].startDoorOpening()
				model.doorOpen(doors[doorWindow[2]]);
			}
		}
		
		public function doorIsReady(d:DoorModel):void {
			trace("[" + d.number + "] READY");
			if (doorWindow[0] == d.number || 
				doorWindow[1] == d.number || 
				doorWindow[2] == d.number) {
					model.doorOpen(d);
					d.startDoorOpening();
				}
		}

	}
	
}
