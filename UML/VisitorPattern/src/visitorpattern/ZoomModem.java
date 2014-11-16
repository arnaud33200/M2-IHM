package visitorpattern;

public class ZoomModem
    extends Modem {
        private double param4 = 2.5d;
    
	public ZoomModem() {}

	public void accept(ModemVisitor v) {
		// 2° Dispatch : en fonction du type statique de this (HayesModem)
		v.visit(this);
	}

        public double getParam4() { return param4; }
        
        public void setParam4(double p4) { param4 = p4; }
}
