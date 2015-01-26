﻿package  {
	
	import flash.utils.Timer;
    import flash.events.TimerEvent;
	import flash.events.*;
	
	public class DoorModel extends EventDispatcher {
		
		public static const SPEED1:int = 800;
		public static const SPEED2:int = 1600;
		public static const SPEED3:int = 2400;
		
		public static const PERSONGOOD:int = 0;
		public static const PERSONBAD:int = 1;
		
		public static const STATECLOSE:int = 0;
		public static const STATECOMING:int = 1;
		public static const STATEREADY:int = 2;
		public static const STATEOPENGOOD:int = 30;
		public static const STATEACTIONGOOD:int = 40;
		public static const STATEOPENBAD:int = 31;
		public static const STATEACTIONBAD:int = 41;
		public static const STATECLOSEACTION:int = 5;
		
		public var doorState:int;
		
		public static const DOOROPENINGTIME:int = 1000;
		public static const DOORACTIONTIME:int = 1000;
		public static const DOORCLOSINGTIME:int = 200;
		
		public var number:int;
		public var speed:int;
		public var person:int;
		
		private var comingTimer:Timer;
		private var openTimer:Timer;
		private var closeTimer:Timer;
		// the time taken by the good or the bad
		private var actionTimer:Timer;
		
		private var doorManager:DoorManager;
		
		public function DoorModel(n:int, d:DoorManager) {
			doorState = STATECLOSE;
			number = n;
			doorManager = d;
			
			openTimer = new Timer(DOOROPENINGTIME, 1);
			openTimer.addEventListener(TimerEvent.TIMER, doorActionBegin);
			closeTimer = new Timer(DOORCLOSINGTIME, 1);
			closeTimer.addEventListener(TimerEvent.TIMER, doorCloseEnd);
			actionTimer = new Timer(DOORACTIONTIME, 1);
			actionTimer.addEventListener(TimerEvent.TIMER, actionIsOver);
		}
		
		public function doComing():void {
			speed = (Math.floor(Math.random() * (3 - 1 + 1)) + 1);
			switch (speed) {
				case 1: speed = SPEED1; break;
				case 2: speed = SPEED2; break;
				case 3: speed = SPEED3; break;
			}
			doorManager.someOneComing(this);
			comingTimer = new Timer(speed, 1);
			comingTimer.addEventListener(TimerEvent.TIMER, doorIsReady);
			comingTimer.start();
		}
		
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		public function someoneComing():void {

			switch (doorState) {
				case STATECLOSE:
					doorState = STATECOMING;
					doComing();
					break;
				case STATECOMING: 
					break;
				case STATEREADY: 
					break;
				case STATEOPENGOOD: 
					break;
				case STATEACTIONGOOD: 
					break;
				case STATEOPENBAD: 
					break;
				case STATEACTIONBAD: 
					break;
				case STATECLOSEACTION: 
					break;
			}
		}
		
		public function doorIsReady(e:TimerEvent):void{
			
			switch (doorState) {
				case STATECLOSE:
					break;
				case STATECOMING:
					doorState = STATEREADY;
					person = (Math.floor(Math.random() * (1 - 0 + 1)) + 0);
					doorManager.doorIsReady(this);
					break;
				case STATEREADY: 
					break;
				case STATEOPENGOOD: 
					break;
				case STATEACTIONGOOD: 
					break;
				case STATEOPENBAD: 
					break;
				case STATEACTIONBAD: 
					break;
				case STATECLOSEACTION: 
					break;
			}
		}
		
		public function startDoorOpening():void {

			switch (doorState) {
				case STATECLOSE:
					break;
				case STATECOMING: 
					break;
				case STATEREADY:
					if (person == PERSONGOOD) {
						//trace("["+ number + "] GOOD - door open");
						doorState = STATEOPENGOOD;
					} else {
						//trace("["+ number + "] BAD - door open");
						doorState = STATEOPENBAD;
					}
					openTimer.start();
					break;
				case STATEOPENGOOD: 
					break;
				case STATEACTIONGOOD: 
					break;
				case STATEOPENBAD: 
					break;
				case STATEACTIONBAD: 
					break;
				case STATECLOSEACTION: 
					break;
			}
		}
		
		public function doorActionBegin(e:TimerEvent):void {		
			switch (doorState) {
				case STATECLOSE:
					break;
				case STATECOMING: 
					break;
				case STATEREADY: 
					break;
				case STATEOPENGOOD:
					doorState = STATEACTIONGOOD;
					actionTimer.start();
					break;
				case STATEACTIONGOOD: 
					break;
				case STATEOPENBAD:
					doorState = STATEACTIONBAD;
					actionTimer.start();
					break;
				case STATEACTIONBAD: 
					break;
				case STATECLOSEACTION: 
					break;
			}
		}
		
		public function shootReceived():void {
			//trace ("[" + number + "] - SHOOT");
			
			switch (doorState) {
				case STATECLOSE:
					doorState = STATECLOSE;
					break;
				case STATECOMING:
					doorState = STATECOMING;
					break;
				case STATEREADY: 
					doorState = STATEREADY;
					break;
				case STATEOPENGOOD: 
					doorState = STATECLOSE;
					dispatchEvent(new DoorEvent(DoorEvent.WRONG_TARGET, this));
					openTimer.stop();
					break;
				case STATEACTIONGOOD: 
					dispatchEvent(new DoorEvent(DoorEvent.WRONG_TARGET, this));
					doorState = STATECLOSE;
					actionTimer.stop();
					break;
				case STATEOPENBAD: 
					doorState = STATECLOSEACTION;
					dispatchEvent(new DoorEvent(DoorEvent.TOO_EARLY, this));
					//trace("boom");
					closeTimer.start();
					openTimer.stop();
					break;
				case STATEACTIONBAD:
					doorState = STATECLOSEACTION;
					closeTimer.start();
					dispatchEvent(new DoorEvent(DoorEvent.GOOD_SHOOT, this));
					actionTimer.stop();
					//trace("BOOM");
					break;
				case STATECLOSEACTION:
					doorState = STATECLOSEACTION;
					break;
			}
		}
		
		public function actionIsOver(e:TimerEvent):void {
			//doorManager.doorActionIsOver(this);
			
			switch (doorState) {
				case STATECLOSE:
					break;
				case STATECOMING: 
					break;
				case STATEREADY: 
					break;
				case STATEOPENGOOD: 
					break;
				case STATEACTIONGOOD:
					doorState = STATECLOSEACTION;
					closeTimer.start();
					dispatchEvent(new DoorEvent(DoorEvent.MONEY, this));
					break;
				case STATEOPENBAD: 
					break;
				case STATEACTIONBAD:
					doorState = STATECLOSE;
					dispatchEvent(new DoorEvent(DoorEvent.TOO_LATE, this));
					break;
				case STATECLOSEACTION: 
					break;
			}
		}
		
		public function doorCloseEnd(e:TimerEvent):void {		
			switch (doorState) {
				case STATECLOSE:
					break;
				case STATECOMING: 
					break;
				case STATEREADY: 
					break;
				case STATEOPENGOOD:
					break;
				case STATEACTIONGOOD: 
					break;
				case STATEOPENBAD:
					break;
				case STATEACTIONBAD: 
					break;
				case STATECLOSEACTION: 
					//trace("close asked");
					doorState = STATECLOSE;
					dispatchEvent(new DoorEvent(DoorEvent.CLOSING_END, this));
					break;
			}
		}

	}
	
}
