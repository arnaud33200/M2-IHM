package visitorpattern;

public class HayesModem
    extends Modem {
        private String param3 = "default setup";
	
	public HayesModem() {}

	public void accept(ModemVisitor v) {
		// 2° Dispatch : en fonction du type statique de this (HayesModem)
		v.visit(this);
	}
        
        public String getParam3() { return param3; }
        public void setParam3(String p3) { param3 = p3; } 
}
