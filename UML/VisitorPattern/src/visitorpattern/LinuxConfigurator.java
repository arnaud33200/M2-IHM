package visitorpattern;

public class LinuxConfigurator
    implements ModemVisitor {
	public LinuxConfigurator() {
	}

	public void visit(HayesModem modem) {
		System.out.println("Configuring a Hayes Modem for Linux");
		// On positionne les paramètres spécifiques à une configuration Linux d'un HayesModem
		modem.setParam3("Linux configuration string...");
	}

	public void visit(ZoomModem modem) {
		System.out.println("Configuring a Zoom Modem for Linux");
		// On positionne les paramètres spécifiques à une configuration Linux d'un ZoomModem
                modem.setParam4(2.0);
	}

	public void visit(MyModem modem) {
		System.out.println("Configuring a MyModem for Linux");
		// On positionne les paramètres spécifiques à une configuration Linux d'un MyModem
                modem.setParam1(2);
                modem.setParam2(false);
        }

}
