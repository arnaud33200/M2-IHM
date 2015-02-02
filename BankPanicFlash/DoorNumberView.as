package  {
	
	import flash.display.MovieClip;
	import flash.text.TextFormat;
	
	public class DoorNumberView extends MovieClip {
		
		private var txt:String;
		
		public function DoorNumberView(t:String) {
			//myText.embedFonts = false;
			txt = t;
			/*var tf:TextFormat = myText.getTextFormat();
			tf.bold = false;
			tf.size = 20;
			myText.setTextFormat(tf);*/
			setNormal();
		}
		
		public function setActive() {
			myText.htmlText = "<b>" + txt + "</b>";
		}
		
		public function setNormal() {
			myText.htmlText = "<font color='#015101'>" + txt + "</font>";
		}
	}
	
}
