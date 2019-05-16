package restServices;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import configuration.jwtConfiguration.JsonTokenNeeded;
import models.Device;
import response.AuthorizationResponse;
import response.BaseResponse;
import response.DeviceCollectionResponse;
import util.JwTokenHelper;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import javax.ws.rs.GET;
import org.bson.Document;
import com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.eq;

@Path("/")
public class HomeApiService extends BaseApiService {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @POST
    @Path("/authorization_service")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizationService(@HeaderParam(USERNAME) String userName, @HeaderParam(PASSWORD) String password) {
        System.out.println("sadfasdfasdfadfs");
        if (userName.isEmpty()) {
            return getResponse(new BaseResponse(USERNAME + " field cannot be empty", BaseResponse.FAILURE));
        } else if (password.isEmpty()) {
            return getResponse(new BaseResponse(PASSWORD + " field cannot be empty", BaseResponse.FAILURE));
        }

        //Realizar la validacion del Usuario contra su base de datos. Debe crear una clase de negocio que se enlace con el DAO.
        //Si la autenticacion es correcta, posteriormente puede proceder a invocar
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase  database = mongoClient.getDatabase("App");
        MongoCollection<Document> collection = database.getCollection("Users");
        Document myDoc = collection.find(eq("Username", userName)).first();
        System.out.println(myDoc.toJson());
        if (myDoc.get("password", String.class).equals( password)) {
            String privateKey = JwTokenHelper.getInstance().generatePrivateKey(userName, password);
            return getResponse(new AuthorizationResponse(BaseResponse.SUCCESS, "You're authenticated successfully. Private key will be valid for 30 mins", privateKey));

        }
        return getResponse(new BaseResponse("Credenciales Erroneas", BaseResponse.FAILURE));

    }

    @GET
    @Path("/allDevices")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDevices() {
        System.out.println("Method called");
        return getResponse(new DeviceCollectionResponse(Arrays.asList(new Device("Electric Kettle", 1, true), new Device("Computer", 2, true), new Device("Motorcycle", 3, false), new Device("Sandwich Maker", 4, true))));
    }
}
