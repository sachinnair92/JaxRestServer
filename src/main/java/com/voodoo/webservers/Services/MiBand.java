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
import org.w3c.dom.Element;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
    String hr;
    String tm;
    int cnt;


    @GET
    @Path("/update_heartrate")
    @Produces("application/json")
    public String update_heartrate(@QueryParam("hospital_name") String hospital_name,@QueryParam("ambulance_id") String ambulance_id ,@QueryParam("p_id") String p_id,@QueryParam("heartrate") String heartrate,@QueryParam("timestamp") String timestamp) {

        obj = new JSONObject();
        try {


            FindIterable<org.bson.Document> iterable = collection.find(new org.bson.Document("hospital_name", hospital_name).append("ambulance_id", ambulance_id).append("p_id",p_id));

            hr="null";
            tm="null";
            cnt=0;
            datafound=false;
            iterable.forEach(new Block<org.bson.Document>() {
                @Override
                public void apply(final org.bson.Document document) {

                    datafound=true;
                    hr=String.valueOf(document.get("heartrate"));
                    tm=String.valueOf(document.get("timestamp"));
                    cnt=Integer.parseInt(String.valueOf(document.get("count")));
                }
            });



            if(datafound==true)
            {
                if(cnt<10)
                {
                    heartrate=hr+heartrate;
                    timestamp=tm+timestamp;
                    cnt++;
                    UpdateResult ur = collection.updateOne(new org.bson.Document("p_id", p_id).append("hospital_name", hospital_name).append("ambulance_id",ambulance_id), new org.bson.Document("$set", new org.bson.Document("heartrate", heartrate+";").append("timestamp",timestamp+";").append("count",cnt)));
                    if (ur.getModifiedCount() != 0) {
                        obj.put("status","true");
                        return String.valueOf(obj);
                    }
                }
                else
                {
                    int cnt1=cnt;

                    cnt1=cnt1%10;
                    String[] hr1=hr.split(";");
                    String[] tm1=tm.split(";");
                    hr1[cnt1]=heartrate;
                    tm1[cnt1]=timestamp;
                    hr="";
                    tm="";
                    for(int i=0;i<hr1.length;i++)
                    {
                        hr=hr+hr1[i]+";";
                        tm=tm+tm1[i]+";";
                    }
                    cnt++;
                    UpdateResult ur = collection.updateOne(new org.bson.Document("p_id", p_id).append("hospital_name", hospital_name).append("ambulance_id",ambulance_id), new org.bson.Document("$set", new org.bson.Document("heartrate", hr).append("timestamp",tm).append("count", cnt)));
                    if (ur.getModifiedCount() != 0) {
                        obj.put("status", "true");
                        return String.valueOf(obj);
                    }
                }

                obj.put("status","false");
                return String.valueOf(obj);
            }else
            {
                org.bson.Document doc1 = new org.bson.Document("p_id", p_id)
                        .append("hospital_name", hospital_name)
                        .append("ambulance_id", ambulance_id)
                        .append("heartrate", heartrate + ";")
                        .append("timestamp",timestamp+";")
                        .append("count","1");
                collection.insertOne(doc1);
                obj.put("status","true");
                return String.valueOf(obj);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return "null";
    }





    @GET
    @Path("/get_heartrate")
    @Produces("application/json")
    public String get_heartrate(@QueryParam("hospital_name") String hospital_name,@QueryParam("ambulance_id") String ambulance_id ,@QueryParam("p_id") String p_id) {

        obj = new JSONObject();
        try {

            FindIterable<org.bson.Document> iterable = collection.find(new org.bson.Document("hospital_name", hospital_name).append("ambulance_id", ambulance_id).append("p_id", p_id));

            datafound = false;
            iterable.forEach(new Block<org.bson.Document>() {
                @Override
                public void apply(final org.bson.Document document) {

                    datafound = true;

                    obj.put("hospital_name",String.valueOf(document.get("hospital_name")));

                    obj.put("ambulance_id",String.valueOf(document.get("ambulance_id")));

                    obj.put("p_id",String.valueOf(document.get("p_id")));

                    obj.put("heartrate",String.valueOf(document.get("heartrate")));

                    obj.put("timestamp",String.valueOf(document.get("timestamp")));

                    obj.put("count",String.valueOf(document.get("count")));


                }
            });


            if (datafound == true) {

                obj.put("status","true");
                return String.valueOf(obj);
            }

            obj.put("status","false");
            return String.valueOf(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "null";
    }



}
