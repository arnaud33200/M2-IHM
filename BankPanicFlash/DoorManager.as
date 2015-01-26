﻿package  {
	
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
				d.addEventListener(DoorEvent.CLOSING_END, closing_end);
				doors[i] = d;
			}
			doorWindow = [0,1,2];
			
			newComingTimer = new Timer(timePeriod, 0);
			newComingTimer.addEventListener(TimerEvent.TIMER, createNewComming);
			newComingTimer.start();
		}
		
		public function getDoorWindow():Array {
			return doorWindow;
		}
		
		public function someOneComing(d:DoorModel) {
			model.someOneComing(d);
		}
		
		public function isAllDoorsClosed():Boolean {
			var rtn:Boolean = true;
			var msg:String = "";
			for (var i:int = 0; i < 3; i++) {
				var c:DoorModel = doors[doorWindow[i]];
				if (c.doorState != DoorModel.STATECLOSE ) {
					rtn = false;
					msg += c.number + " - ";
				}
			}
			trace("Not Closed : " + msg);
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
		
		private function too_early(e:DoorEvent):void {
			trace("[" + e.door.number + "] too early !!!");
			model.doorClose(e);
		}
		private function good_shoot(e:DoorEvent):void {
			trace("[" + e.door.number + "] BOOM NICE SHOT !!!");
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
			model.gameLoose(e);
		}
		private function wrong_target(e:DoorEvent):void {
			trace("[" + e.door.number + "] WRONG !!!");
			model.doorClose(e);
			model.gameLoose(e);
		}
		private function closing_end(e:DoorEvent):void {
			model.doorClose(e);
		}
		
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
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
			//trace(".... CHECK if ready");
			if (doors[doorWindow[0]].doorState == DoorModel.STATEREADY) {
				doors[doorWindow[0]].startDoorOpening()
				model.doorOpen(doors[doorWindow[0]]);
			}
			if (doors[doorWindow[1]].doorState == DoorModel.STATEREADY) {
				doors[doorWindow[1]].startDoorOpening()
				model.doorOpen(doors[doorWindow[1]]);
			}
			if (doors[doorWindow[2]].doorState == DoorModel.STATEREADY) {
				doors[doorWindow[2]].startDoorOpening()
				model.doorOpen(doors[doorWindow[2]]);
			}
		}
		
		public function doorIsReady(d:DoorModel):void {
			var msg:String = "READY - ";
			for each (var door:DoorModel in doors) {
				if (door.doorState == DoorModel.STATEREADY)
					msg += "[" + door.number + "] -";
			}
			//trace(msg);
			if (doorWindow[0] == d.number || 
				doorWindow[1] == d.number || 
				doorWindow[2] == d.number) {
					model.doorOpen(d);
					d.startDoorOpening();
				}
		}

	}
	
}
