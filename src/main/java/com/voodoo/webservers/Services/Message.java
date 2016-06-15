package com.voodoo.webservers.Services;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.swagger.annotations.Api;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by voodoo on 15/6/16.
 */

@Api(value = "Message")
@Path("/Message/")
public class Message {

    MongoClientURI connectionString = new MongoClientURI("mongodb://voodoo:722446@ds057862.mlab.com:57862/prms");
    MongoClient mongoClient = new MongoClient(connectionString);
    MongoDatabase db = mongoClient.getDatabase(connectionString.getDatabase());
    MongoCollection<Document> collection = db.getCollection("Messages");
    JSONObject obj = new JSONObject();
    JSONArray ja;
    Boolean datafound;
    int count;


    @GET
    @Path("/set_Message")
    @Produces("application/json")
    public String set_Message(@QueryParam("User_Name") String S_uname,@QueryParam("User_Name")  String S_time,@QueryParam("User_Name")  String R_hosp_name,@QueryParam("User_Name")  String R_amb_id,@QueryParam("User_Name")  String R_pid,@QueryParam("User_Name") String msg,@QueryParam("User_Name") String Is_amb) {
        obj = new JSONObject();
        try {


            org.bson.Document doc1 = new org.bson.Document("S_uname", S_uname)
                    .append("S_time", S_time)
                    .append("R_hosp_name", R_hosp_name)
                    .append("R_amb_id", R_amb_id)
                    .append("R_pid", R_pid)
                    .append("msg", msg)
                    .append("Is_amb", Is_amb);

            collection.insertOne(doc1);

            obj.put("status","true");
            return String.valueOf(obj);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return "null";

    }



    @GET
    @Path("/get_Messages")
    @Produces("application/json")
    public String get_Messages(@QueryParam("hospital_name") String hospital_name,@QueryParam("ambulance_id") String ambulance_id,@QueryParam("p_id") String p_id) {

        obj = new JSONObject();
        try {

            FindIterable<Document> iterable = collection.find(new org.bson.Document("R_hosp_name", hospital_name).append("R_amb_id", ambulance_id).append("R_pid", p_id));


            datafound = false;
            count=0;
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final org.bson.Document document) {
                    ja = new JSONArray();
                    JSONObject jo = new JSONObject();

                    datafound = true;

                    jo.put("S_uname", "S_uname");

                    jo.put("S_time", "S_time");

                    jo.put("R_hosp_name", "R_hosp_name");

                    jo.put("R_amb_id", "R_amb_id");

                    jo.put("R_pid", "R_pid");

                    jo.put("msg", "msg");

                    jo.put("Is_amb", "Is_amb");

                    ja.put(jo);

                    obj.put("Message_"+count, ja);

                    count++;

                }
            });


            if (datafound == true) {

                obj.put("status","true");
                obj.put("count",count);

                return String.valueOf(obj);
            }

            obj.put("status","false");
            return String.valueOf(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
