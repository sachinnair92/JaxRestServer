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
import org.w3c.dom.Element;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Random;

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





    public int gen() {
        Random r = new Random( System.currentTimeMillis() );
        return 10000 + r.nextInt(20000);
    }



    @GET
    @Path("/register_user")
    @Produces("application/json")
    public String register_user(@QueryParam("User_Name") String User_Name,@QueryParam("Password") String Password,@QueryParam("Hospital_name") String Hospital_name,@QueryParam("Type_of_User")String Type_of_User) {
        obj = new JSONObject();

        try {
            is_valid=0;
            FindIterable<org.bson.Document> iterable = collection.find(new org.bson.Document("user_name", User_Name));
            iterable.forEach(new Block<org.bson.Document>() {
                @Override
                public void apply(final org.bson.Document document) {
                    is_valid=1;
                }

            });


            if(is_valid==1)
            {
                obj.put("status", "false");
                return String.valueOf(obj);
            }



            Document doc;
            if(Type_of_User.equals("Doctor")) {
                doc = new org.bson.Document("user_name", User_Name)
                        .append("password", Password)
                        .append("hospital_name", Hospital_name)
                        .append("type_of_user", Type_of_User)
                        .append("ambulance_id", "null");

            }else
            {
                String temp="amb_"+String.valueOf(gen());
                doc = new org.bson.Document("user_name", User_Name)
                        .append("password", Password)
                        .append("hospital_name", Hospital_name)
                        .append("type_of_user", Type_of_User)
                        .append("ambulance_id", temp);

                obj.put("ambulance_id", temp);

            }

            collection.insertOne(doc);
            obj.put("status", "true");
            return String.valueOf(obj);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "null";
    }


    String tou,aid,hname,pwd;



    @GET
    @Path("/validate_user")
    @Produces("application/json")
    public String validate_user(@QueryParam("user_name") String user_name,@QueryParam("password") String password) {
        obj = new JSONObject();
        try {


            is_valid=0;
            pwd=null;
            tou=null;
            aid=null;
            hname=null;
            FindIterable<org.bson.Document> iterable = collection.find(new org.bson.Document("user_name", user_name));
            iterable.forEach(new Block<org.bson.Document>() {
                @Override
                public void apply(final org.bson.Document document) {
                    is_valid=1;
                    pwd = String.valueOf(document.get("password"));
                    tou = String.valueOf(document.get("type_of_user"));
                    aid = String.valueOf(document.get("ambulance_id"));
                    hname = String.valueOf(document.get("hospital_name"));
                }

            });



            if(is_valid==1 && pwd.equals(password)){

                obj.put("type_of_user", tou);
                if(tou.equals("Ambulance Staff"))
                {
                    obj.put("ambulance_id",aid);
                }


                obj.put("hospital_name",hname);

                obj.put("status","true");

                return String.valueOf(obj);

            }
            else
            {
                obj.put("status","false");
                return String.valueOf(obj);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }


        return "null";
    }







}
