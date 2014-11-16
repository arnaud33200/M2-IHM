package visitorpattern;

public class MyModem
    extends Modem {
        private int param1 = 5;
        private boolean param2 = false;
	
	public MyModem() {}

	public void accept(ModemVisitor v) {
		// 2° Dispatch : en fonction du type statique de this (MyModem)
		v.visit(this);
	}

        public int getParam1() { return param1; }
        public void setParam1(int p1) { param1 = p1; }
        public boolean getParam2() { return param2; }
        public void setParam2(boolean p2) { param2 = p2; }
}
