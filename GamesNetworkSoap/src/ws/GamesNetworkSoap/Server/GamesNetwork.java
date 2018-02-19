package ws.GamesNetworkSoap.Server;


import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.io.OutputStream;


import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ws.GamesNetworkSoap.exceptions.ConnectionException;
import ws.GamesNetworkSoap.exceptions.InternalErrorException;
import ws.GamesNetworkSoap.exceptions.UnauthorizedException;
import ws.GamesNetworkSoap.model.Usermodel;

@WebService(endpointInterface = "ws.GamesNetworkSoap.Server.IGamesNetwork")
public class GamesNetwork implements IGamesNetwork {

    @Resource
    WebServiceContext context;

    @Override
    public double add(double term1, double term2) {
        // TODO Auto-generated method stub
        return term1 + term2;
    }

    //
    //devuelve t
    //necesita el usuario y el token
    //
    //
    @Override
    public List<Usermodel> GetUsers(String tok) throws UnauthorizedException, InternalErrorException, ConnectionException {
        if (isAuthenticated(tok)) {
            List<Usermodel> usuarios = new ArrayList<Usermodel>();
            DatabaseConnection connection = DatabaseConnection.getDbCon();
            if (connection != null) {
                System.out.println("You made it, take control your database now!");

                try {
                    ResultSet rs = connection.query("SELECT * FROM usuario ");

                    while (rs.next()) {
                        Usermodel usuario = new Usermodel();
                        usuario.setId(rs.getInt("id"));
                        usuario.setPassword(rs.getString("password"));
                        usuario.setNombre(rs.getString("nombre"));
                        usuario.setEmail(rs.getString("email"));
                        usuario.setUsername(rs.getString("username"));
                        usuario.setLocalization(rs.getString("localizacion"));
                        usuario.setEdad(rs.getInt("edad"));
                        usuarios.add(usuario);
                    }
                    rs.close();
                } catch (SQLException e) {
                    throw new InternalErrorException("Error al hacer la petición");
                }

            } else {
                System.out.println("Failed to make connection!");
            }
            return usuarios;
        } else {
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new UnauthorizedException("No tienes permisos para hacer esta petición");

        }
    }


    //
    //devuelve el codigo del resultado SQL de crear un nuevo usuario
    //necesita el usuario y el token
    //
    //
    @Override
    public int CreateUser(Usermodel user, String tok) throws UnauthorizedException, InternalErrorException, ConnectionException {

        if (isAuthenticated(tok)) {
            DatabaseConnection connection = DatabaseConnection.getDbCon();

            int result = 0;
            if (connection != null) {
                String select = "SELECT MAX(id) FROM usuario; ";
                try {
                    ResultSet rs = connection.query(select);
                    rs.next();
                    int id = rs.getInt(1);
                    id++;
                    String sql = "INSERT INTO usuario (nombre,username,password,edad,email,id) " + "VALUES ('"
                            + user.getNombre() + "','" + user.getUsername() + "', '" + user.getPassword() + "', "
                            + user.getEdad() + ",'" + user.getEmail() + "'," + id + " );";

                    System.out.println(sql);

                    result = connection.update(sql);
                }catch (SQLException e) {
                    throw new InternalErrorException("Error creando el usuario");
                }

            } else {
                throw new ConnectionException("Fallo al hacer la conexión");
            }

            return result;
        } else {
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new UnauthorizedException("No tienes permisos para hacer esta petición");

        }
    }


    //
    //devuelve el codigo del resultado SQL de borrar un nuevo usuario
    //necesita el usuario y el token
    //
    //
    @Override
    public int DeleteUser(Usermodel user, String tok) throws UnauthorizedException, InternalErrorException, ConnectionException {

        if (isAuthenticated(tok)) {
            DatabaseConnection connection = DatabaseConnection.getDbCon();
            int result = 0;
            if (connection != null) {
                String deltequery = "DELETE from usuario where ID = " + user.getId() + ";";
                try {
                    result = connection.update(deltequery);
                } catch (SQLException e) {
                    throw new InternalErrorException("Error eliminando el usuario");
                }
            } else {
                throw new ConnectionException("Fallo al hacer la conexión");
            }

            return result;

        } else {
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new UnauthorizedException("No tienes permisos para hacer esta petición");

        }
    }

    //
    //devuelve el codigo del resultado SQL de actualizar un nuevo usuario
    //necesita el usuario y el token
    //
    //
    @Override
    public int UpdateUser(Usermodel user, String tok) throws UnauthorizedException, InternalErrorException, ConnectionException {

        if (isAuthenticated(tok)) {
            DatabaseConnection connection = DatabaseConnection.getDbCon();
            int result = 0;
            if (connection != null) {
                String sql = " UPDATE usuario set nombre='" + user.getNombre() + "',edad=" + user.getEdad()
                        + ",username ='" + user.getUsername() + "',password='" + user.getPassword() + "' WHERE id="
                        + user.getId() + ";";

                System.out.println(sql);

                try {
                    result = connection.update(sql);
                } catch (SQLException e) {
                    throw new InternalErrorException("No se ha podido realizar la peticion");
                }

            } else {
                throw new ConnectionException("Fallo al hacer la conexión");
            }

            return result;
        } else {
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new UnauthorizedException("No tienes permisos para hacer esta petición");

        }

    }


    //
    //Obtiene toda la infromacion del usuario segun el id
    //necesita el usuario y el token
    //
    //
    @Override
    public Usermodel GetUser(Usermodel user, String tok) throws ConnectionException, UnauthorizedException, InternalErrorException {
        try {
            if (isAuthenticated(tok)) {
                DatabaseConnection connection = DatabaseConnection.getDbCon();
                Usermodel usuario = new Usermodel();
                if (connection != null) {
                    ResultSet rs = null;
                    rs = connection.query("SELECT * FROM usuario WHERE id = " + user.getId() + ";");
                    rs.next();
                    usuario.setId(rs.getInt("id"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setLocalization(rs.getString("localizacion"));
                    usuario.setEdad(rs.getInt("edad"));
                    System.out.print("Column 1 returned ");
                    System.out.println(rs.getString("nombre"));

                } else {
                    throw new ConnectionException("Fallo al hacer la conexión");
                }

                return usuario;

            } else {
                System.out.println("excepcion");
                MessageContext ctx = context.getMessageContext();
                HttpServletResponse response = (HttpServletResponse)
                        ctx.get(MessageContext.SERVLET_RESPONSE);
                throw new UnauthorizedException("No tienes permisos para hacer esta petición");
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Error interno del servidor");
        }
    }

    @Override
    public String Loggin(String user, String pass) throws
            UnauthorizedException, ConnectionException, InternalErrorException {
        if (!user.equals("admin")) {
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new UnauthorizedException("No eres el administrador");
        }
        try {

            URL url = new URL("http://localhost:3000/api/Usuarios/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"username\":\"" + user + "\",\"password\":\"" + pass + "\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

                if(conn.getResponseCode()==HttpURLConnection.HTTP_UNAUTHORIZED){
                    throw new UnauthorizedException("Las credenciales no son válidas");
                }

                throw new ConnectionException("Error al conectarse con el servidor de Loopback");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                System.out.println(inputLine);
            }

            br.close();

            JSONObject myResponse = new JSONObject(response.toString());


            conn.disconnect();
            return myResponse.getString("id");

        } catch (MalformedURLException e) {

            e.printStackTrace();
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new InternalErrorException("Error interno en el servidor. Malformed URL");

        } catch (IOException | JSONException e) {

            e.printStackTrace();
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new InternalErrorException("Hubo un error interno en el servidor");

        }


    }

    public boolean isAuthenticated(String tok) throws UnauthorizedException, InternalErrorException {
        if (tok == null) return false;
        HttpURLConnection conn = null;
        BufferedReader br = null;
        try {
            String myurl = "http://localhost:3000/api/Usuarios/1/accessTokens?filter=%7B%22where%22%3A%7B%22id%22%3A%22" + tok + "%22%7D%7D&access_token=" + tok;
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        } catch (IOException e) {
            System.out.println("entro");
            try {
                if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    System.out.println(conn.getResponseCode());
                    MessageContext ctx = context.getMessageContext();
                    HttpServletResponse response = (HttpServletResponse)
                            ctx.get(MessageContext.SERVLET_RESPONSE);
                    throw new UnauthorizedException("No tienes permisos para hacer esta petición");

                }
            } catch (IOException e1) {
                throw new InternalErrorException("Error interno del servidor");
            }
            return false;
        }

        try {
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                System.out.println(inputLine);
            }
            br.close();
            JSONArray jarray = new JSONArray(response.toString());
            JSONObject myResponse = jarray.getJSONObject(0);
            String token = myResponse.getString("id");
            int ttl = myResponse.getInt("ttl");
            String date = myResponse.getString("created");
            conn.disconnect();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date convertedCurrentDate = sdf.parse(date);
            System.out.println(convertedCurrentDate.getTime() / 1000);
            Date currentdate = new Date();
            if (((currentdate.getTime() - convertedCurrentDate.getTime()) / 1000) > ttl)
                return false;
            else
                return true;
        } catch (IOException | ParseException | JSONException e) {
            throw new InternalErrorException("Error interno del servidor");
        }
    }


    //Desloguea del sistema.
    //devuelve por sesion 204 si logra desconectarse. 500 de lo contrario
    @Override
    public void Logout(String tok) throws InternalErrorException, UnauthorizedException {

        try {

            URL url = new URL("http://localhost:3000/api/Usuarios/logout?access_token=" + tok);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            br.close();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {


                MessageContext ctx = context.getMessageContext();
                HttpServletResponse response = (HttpServletResponse)
                        ctx.get(MessageContext.SERVLET_RESPONSE);
                response.sendError(204);

            } else {


                MessageContext ctx = context.getMessageContext();
                HttpServletResponse response = (HttpServletResponse)
                        ctx.get(MessageContext.SERVLET_RESPONSE);
                throw new InternalErrorException("Ha sucedido un error en el servidor");


            }


            conn.disconnect();


        } catch (MalformedURLException e) {

            e.printStackTrace();
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new InternalErrorException("La URL no es válida");

        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("excepcion");
            MessageContext ctx = context.getMessageContext();
            HttpServletResponse response = (HttpServletResponse)
                    ctx.get(MessageContext.SERVLET_RESPONSE);
            throw new UnauthorizedException("No se puede realizar esta petición");


        }

    }


}
