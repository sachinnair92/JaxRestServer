package com.voodoo.webservers.Services;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
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

@Api(value = "HeartRate")
@Path("/HeartRate/")
public class MiBand {

    MongoClientURI connectionString = new MongoClientURI("mongodb://voodoo:722446@ds057862.mlab.com:57862/prms");
    MongoClient mongoClient = new MongoClient(connectionString);
    MongoDatabase db = mongoClient.getDatabase(connectionString.getDatabase());
    MongoCollection<Document> collection = db.getCollection("heartrate_details");
    JSONObject obj = new JSONObject();
    boolean datafound;


    @GET
    @Path("/update_heartrate")
    @Produces("application/json")
    public String update_heartrate(@QueryParam("hospital_name") String hospital_name,@QueryParam("ambulance_id") String ambulance_id ,@QueryParam("p_name") String p_name,@QueryParam("heartrate") String heartrate) {

        obj = new JSONObject();
        try {


            FindIterable<Document> iterable = collection.find(new Document("hospital_name", hospital_name).append("ambulance_id",ambulance_id).append("p_name",p_name));

            datafound=false;
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    datafound=true;
                }
            });


            if(datafound==true)
            {
                UpdateResult ur = collection.updateOne(new Document("p_name", p_name).append("hospital_name", hospital_name).append("ambulance_id",ambulance_id), new Document("$set", new Document("heartrate", heartrate)));
                if (ur.getModifiedCount() != 0) {
                    obj.put("message", "true");
                    return String.valueOf(obj);
                }
                obj.put("message", "false");
                return String.valueOf(obj);
            }else
            {
                Document doc = new Document("p_name", p_name)
                        .append("hospital_name", hospital_name)
                        .append("ambulance_id", ambulance_id)
                        .append("heartrate",heartrate);
                collection.insertOne(doc);
                obj.put("message", "true");
                return String.valueOf(obj);
            }

        }
        catch(Exception e)
        {
            obj.put("message", "false");
            return String.valueOf(obj);
        }
    }



    @GET
    @Path("/get_heartrate")
    @Produces("application/json")
    public String get_heartrate(@QueryParam("hospital_name") String hospital_name,@QueryParam("ambulance_id") String ambulance_id ,@QueryParam("p_name") String p_name) {

        obj = new JSONObject();
        try {
            FindIterable<Document> iterable = collection.find(new Document("hospital_name", hospital_name).append("ambulance_id",ambulance_id).append("p_name",p_name));

            datafound=false;
            iterable.forEach(new Block<Document>() {
                @Override
                public void apply(final Document document) {
                    datafound=true;
                    obj.put("hospital_name", document.get("hospital_name"));
                    obj.put("ambulance_id", document.get("ambulance_id"));
                    obj.put("p_name", document.get("p_name"));
                    obj.put("heartrate",document.get("heartrate"));
                }
            });


            if(datafound==true)
            {
                obj.put("message", "true");
                return String.valueOf(obj);
            }

                obj.put("message", "false");
                return String.valueOf(obj);

        }
        catch(Exception e)
        {
            obj.put("message", "false");
            return String.valueOf(obj);
        }
    }



}
