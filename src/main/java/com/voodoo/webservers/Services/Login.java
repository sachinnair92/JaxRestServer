package com.voodoo.webservers.Services;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.swagger.annotations.Api;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Created by voodoo on 26/5/16.
 */

@Api(value = "Login")
@Path("/Login/")
public class Login {

    MongoClientURI connectionString = new MongoClientURI("mongodb://voodoo:722446@ds057862.mlab.com:57862/prms");
    MongoClient mongoClient = new MongoClient(connectionString);
    MongoDatabase db = mongoClient.getDatabase(connectionString.getDatabase());
    MongoCollection<Document> collection = db.getCollection("credentials");
    JSONObject obj = new JSONObject();
    int is_valid=0;
    String pwd=null;

    @GET
    @Path("/register_user")
    @Produces("application/json")
    public String register_user(@QueryParam("User_Name") String User_Name,@QueryParam("Password") String Password,@QueryParam("Hospital_name") String Hospital_name,@QueryParam("Type_of_User")String Type_of_User) {
        obj = new JSONObject();
        try {
            Document doc = new Document("user_name", User_Name)
                    .append("password", Password)
                    .append("hospital_name", Hospital_name)
                    .append("type_of_user", Type_of_User)
                    .append("ambulance_id", "null");
            collection.insertOne(doc);
            obj.put("message", "true");
            return String.valueOf(obj);
        }
        catch(Exception e)
        {
            obj.put("message", "false");
            return String.valueOf(obj);
        }
    }

    @GET
    @Path("/register_ambulance")
    @Produces("application/json")
    public String register_ambulance(@QueryParam("User_Name") String User_Name,@QueryParam("Password") String Password,@QueryParam("Hospital_name") String Hospital_name,@QueryParam("Type_of_User")String Type_of_User,@QueryParam("ambulance_id")String ambulance_id) {
        obj = new JSONObject();
        try {
            Document doc = new Document("user_name", User_Name)
                    .append("password", Password)
                    .append("hospital_name", Hospital_name)
                    .append("type_of_user", Type_of_User)
                    .append("ambulance_id", ambulance_id);
            collection.insertOne(doc);
            obj.put("message", "true");
            return String.valueOf(obj);
        }
        catch(Exception e)
        {
            obj.put("message", "false");
            return String.valueOf(obj);
        }
    }


    @GET
    @Path("/validate_user")
    @Produces("application/json")
    public String validate_user(@QueryParam("User_Name") String User_Name,@QueryParam("Password") String Password) {
        obj = new JSONObject();
        try {
            is_valid=0;

            FindIterable<Document> iterable = collection.find(new Document("user_name", User_Name));
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    is_valid=1;
                    pwd = String.valueOf(document.get("password"));
                }

            });



            if(is_valid==1 && pwd.equals(Password)){

                    obj.put("message", "true");
                    return String.valueOf(obj);
            }
            else
            {
                obj.put("message", "false");
                return String.valueOf(obj);
            }

        }
        catch(Exception e)
        {
            obj.put("message", "false");
            return String.valueOf(obj);
        }
    }







}
