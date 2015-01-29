package  {
	
	import flash.display.MovieClip;
	
	
	public class DoorComingView extends MovieClip {
		
		private var doorStateViews:Array;
		private var doorNumberViews:Array;
		private var doorDollarsViews:Array;
		
		private var model:GameModel;
		
		public function DoorComingView(m:GameModel) {
			
			model = m;
			model.addEventListener(GameEvent.DOORS_MOVING_LEFT, windowMoved);
			model.addEventListener(GameEvent.DOORS_MOVING_RIGHT, windowMoved);
			model.addEventListener(GameEvent.DOOR_MONEY, doorMoney);
			model.addEventListener(GameEvent.DOOR_COMING, doorComing);
			model.addEventListener(GameEvent.DOOR_OPEN, doorOpen);
			
			this.x = 359;
			this.y = 67;
			
			doorStateViews = new Array(12);
			for (var i:int=0; i<12; ++i) {
				var sv:MovieClip = new DoorStateView();
				sv.y = 36;
				sv.x = -330 + (i * 60);
				addChild(sv);
				doorStateViews[i] = sv;
			}
			
			doorNumberViews = new Array();
			for (var j:int=0; j<12; ++j) {
				var nv:MovieClip = new DoorNumberView("" + (j+1));
				nv.y = 32;
				nv.x = -330 + (j * 60);
				if (j <= 2) {
					nv.setActive();
				}
				addChild(nv);
				doorNumberViews[j] = nv;
			}
			
			doorDollarsViews = new Array();
			for (var k:int=0; k<12; ++k) {
				var dv:MovieClip = new DoorDollarsView();
				dv.y = 8;
				dv.x = -330 + (k * 60);
				dv.gotoAndStop(1);
				if (model.goldCollected[k] > 0) {
					dv.gotoAndStop(15);
				}
				addChild(dv);
				doorDollarsViews[k] = dv;
			}
		}
		
		function windowMoved(e:GameEvent):void {
			for (var j:int=0; j<12; ++j) {
				var nv:MovieClip = doorNumberViews[j];
				nv.setNormal();
				addChild(nv);
			}
			var dw = e.window;
			doorNumberViews[dw[0]].setActive();
			doorNumberViews[dw[1]].setActive();
			doorNumberViews[dw[2]].setActive();
		}
		
		function doorMoney(e:GameEvent):void {
			var n:int = e.numberOpen;
			var dv:MovieClip = doorDollarsViews[n];
			dv.gotoAndPlay(1);
		}
		
		function doorComing(e:GameEvent):void {
			var n:int = e.numberOpen;
			var s:int = e.door.speed;
			var sv:MovieClip = doorStateViews[n];
			switch (s) {
				case DoorModel.SPEED1: sv = new DoorStateView800(); break;
				case DoorModel.SPEED2: sv = new DoorStateView1600(); break;
				case DoorModel.SPEED3: sv = new DoorStateView2400(); break;
			}
			sv.y = 36;
			sv.x = -330 + (n * 60);
			sv.gotoAndPlay(1);
			addChild(sv);
		}
		
		function doorOpen(e:GameEvent):void {
			var n:int = e.door.number;
			var sv:MovieClip = doorStateViews[n];
			sv =  new DoorStateView();
			sv.y = 36;
			sv.x = -330 + (n * 60);
			addChild(sv);
		}
	}
	
}
