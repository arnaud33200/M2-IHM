package visitorpattern;
import java.util.LinkedList;
import java.util.List;
public class Main {

	public static void main(String[] args) {
		List<Modem> modems = new LinkedList<Modem>();
		modems.add(new MyModem());
		modems.add(new ZoomModem());
		modems.add(new HayesModem());
		// Configure all modems for Linux
		ModemVisitor v = new LinuxConfigurator();
		for(Modem m : modems) // Type statique de m == Modem
                        // 1° Dispatch : en fonction du type dynamique de m
			m.accept(v);
		// Print all configurations in HTML
		v = new HTMLPrinter();
		for(Modem m : modems)
			m.accept(v);
	}
}
