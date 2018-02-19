package ws.GamesNetworkSoap.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "UnauthorizedException")
public class InternalErrorException extends Exception {
    public InternalErrorException(String mensaje) {
        super(mensaje);
    }
}
