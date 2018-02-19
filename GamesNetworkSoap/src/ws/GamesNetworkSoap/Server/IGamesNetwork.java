package ws.GamesNetworkSoap.Server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod; 
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebServiceContext;

import org.json.JSONException;

import ws.GamesNetworkSoap.exceptions.ConnectionException;
import ws.GamesNetworkSoap.exceptions.InternalErrorException;
import ws.GamesNetworkSoap.exceptions.UnauthorizedException;
import ws.GamesNetworkSoap.model.Usermodel;

@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface IGamesNetwork {


	@WebMethod
	public double add(double term1, double term2);
	public List<Usermodel> GetUsers(String tok) throws UnauthorizedException, InternalErrorException, ConnectionException;
	public int CreateUser(Usermodel user,String tok) throws UnauthorizedException, InternalErrorException, ConnectionException;
	public Usermodel GetUser(Usermodel user,String tok) throws ConnectionException, UnauthorizedException, InternalErrorException;
	public int DeleteUser(Usermodel user,String tok) throws UnauthorizedException, InternalErrorException, ConnectionException;
	public int UpdateUser(Usermodel user,String tok) throws UnauthorizedException, InternalErrorException, ConnectionException;
	public String Loggin(String user,String pass) throws UnauthorizedException, ConnectionException, InternalErrorException;
	public void Logout(String tok) throws InternalErrorException, UnauthorizedException;
	boolean isAuthenticated(String tok) throws UnauthorizedException, InternalErrorException;
}
