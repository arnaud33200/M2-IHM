package visitorpattern;

public class WindowsConfigurator
    implements ModemVisitor {
	public WindowsConfigurator() {
	}

	public void visit(HayesModem modem) {
		System.out.println("Configuring a Hayes Modem for Windows");
		modem.setParam3("Windows");
	}

	public void visit(ZoomModem modem) {
		System.out.println("Configuring a Zoom Modem for Windows");
		modem.setParam4(1.0);
	}

	public void visit(MyModem modem) {
		System.out.println("Configuring a MyModem for Windows");
		modem.setParam1(5);
                modem.setParam2(true);
	}

}
