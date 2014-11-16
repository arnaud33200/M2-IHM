package visitorpattern;

public interface ModemVisitor {
        // Une méthode pour chaque sous-classe de Modem
	void visit(HayesModem modem);
	void visit(ZoomModem modem);
	void visit(MyModem modem);
}
