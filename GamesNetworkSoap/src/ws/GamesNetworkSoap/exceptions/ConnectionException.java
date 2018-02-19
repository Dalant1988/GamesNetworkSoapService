package ws.GamesNetworkSoap.exceptions;

import javax.xml.ws.WebFault;
@WebFault(name = "ConnectionException")
public class ConnectionException extends Exception {
    public ConnectionException(String mensaje) {
        super(mensaje);
    }
}
