package ws.GamesNetworkSoap.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "UnauthorizedException")
public class UnauthorizedException extends Exception{
    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }
}