package visitorpattern;

public class XMLPrinter
    implements ModemVisitor {
	public XMLPrinter() {
	}

	public void visit(HayesModem modem) {
		System.out.println("Printing XML Configuration for a Hayes Modem");
		System.out.println("<xml>");
                System.out.println("<parameter name=\"p3\" value=\"" + modem.getParam3()+"\"");
                System.out.println("</xml>");
	}

	public void visit(ZoomModem modem) {
		System.out.println("Printing XML Configuration for a Zoom Modem");
		System.out.println("<xml>");
                System.out.println("<parameter name=\"p4\" value=\"" + modem.getParam4()+"\"");
                System.out.println("</xml>");
	}

	public void visit(MyModem modem) {
		System.out.println("Printing XML Configuration for a My Modem");
		System.out.println("<xml>");
                System.out.println("<parameter name=\"p1\" value=\"" + modem.getParam1()+"\"");
                System.out.println("<parameter name=\"p2\" value=\"" + modem.getParam2()+"\"");
                System.out.println("</xml>");
	}
}
