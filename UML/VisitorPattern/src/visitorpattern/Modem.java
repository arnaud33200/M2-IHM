package visitorpattern;

public abstract class Modem {
	public Modem() {
	}
        
        public void dial() {}
        public void hangup() {}
        public void send(){}
        public void receive() {}
    
	public abstract void accept(ModemVisitor v);
}
