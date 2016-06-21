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
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;

import javax.jws.WebMethod;
import javax.jws.WebParam;
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

@Api(value = "Patient")
@Path("/Patient/")
public class Patient {

    MongoClientURI connectionString = new MongoClientURI("mongodb://voodoo:722446@ds057862.mlab.com:57862/prms");
    MongoClient mongoClient = new MongoClient(connectionString);
    MongoDatabase db = mongoClient.getDatabase(connectionString.getDatabase());
    MongoCollection<Document> collection = db.getCollection("patient_details");
    JSONObject obj = new JSONObject();
    JSONArray ja;
    boolean patientfound=false;
    Boolean datafound;
    int count;



    public int gen() {
        Random r = new Random( System.currentTimeMillis() );
        return 10000 + r.nextInt(20000);
    }




    @GET
    @Path("/add_new_patient")
    @Produces("application/json")
    public String add_new_patient(@QueryParam("hospital_name") String hospital_name,@QueryParam("ambulance_id") String ambulance_id ,@QueryParam("p_name") String p_name,@QueryParam("gender") String gender,@QueryParam("blood_grp") String blood_grp,@QueryParam("condition")String condition,@QueryParam("problem")String problem,@QueryParam("police_case")String police_case,@QueryParam("is_enabled")String is_enabled) {
        obj = new JSONObject();
        try {


            String p_id="Patient_"+String.valueOf(gen());
            if(p_name.equals("null"))
            {
                p_name=p_id;
            }
            collection.updateMany(new org.bson.Document("hospital_name", hospital_name).append("ambulance_id",ambulance_id), new org.bson.Document("$set", new org.bson.Document("is_enabled","No")));


            org.bson.Document doc1 = new org.bson.Document("hospital_name", hospital_name)
                    .append("ambulance_id", ambulance_id)
                    .append("p_name", p_name)
                    .append("p_id", p_id)
                    .append("gender", gender)
                    .append("blood_grp", blood_grp)
                    .append("condition", condition)
                    .append("problem", problem)
                    .append("police_case", police_case)
                    .append("is_enabled", is_enabled);

            collection.insertOne(doc1);
            obj.put("P_id", p_id);
            obj.put("status", "true");
            return String.valueOf(obj);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return "null";
    }



    @GET
    @Path("/update_patient")
    @Produces("application/json")
    public String update_patient(@QueryParam("hospital_name") String hospital_name,@QueryParam("ambulance_id") String ambulance_id ,@QueryParam("p_name") String p_name,@QueryParam("p_name") String p_id,@QueryParam("gender") String gender,@QueryParam("blood_grp") String blood_grp,@QueryParam("condition")String condition,@QueryParam("problem")String problem,@QueryParam("police_case")String police_case,@QueryParam("is_enabled")String is_enabled) {

        obj = new JSONObject();
        try {


            UpdateResult ur = collection.updateOne(new org.bson.Document("p_id", p_id).append("hospital_name", hospital_name).append("ambulance_id",ambulance_id), new org.bson.Document("$set", new org.bson.Document("hospital_name", hospital_name).append("ambulance_id", ambulance_id).append("p_name",p_name).append("gender",gender).append("blood_grp",blood_grp).append("condition",condition).append("problem",problem).append("police_case",police_case).append("is_enabled",is_enabled)));


            if (ur.getModifiedCount() != 0) {
                obj.put("status", "true");
                return String.valueOf(obj);
            }

            obj.put("status", "false");
            return String.valueOf(obj);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        return "null";
    }




    @GET
    @Path("/get_patient_details")
    @Produces("application/json")
    public String get_patient_details(@QueryParam("hospital_name") String hospital_name,@QueryParam("ambulance_id") String ambulance_id ,@QueryParam("p_id") String p_id) {

        obj = new JSONObject();
        try {

            FindIterable<org.bson.Document> iterable = collection.find(new org.bson.Document("hospital_name", hospital_name).append("ambulance_id",ambulance_id).append("p_id",p_id));


            patientfound = false;
            iterable.forEach(new Block<org.bson.Document>() {
                @Override
                public void apply(final org.bson.Document document) {
                    patientfound = true;

                    obj.put("hospital_name", document.get("hospital_name"));

                    obj.put("ambulance_id", document.get("ambulance_id"));

                    obj.put("p_name", document.get("p_name"));

                    obj.put("p_id", document.get("p_id"));

                    obj.put("gender", document.get("gender"));

                    obj.put("blood_grp", document.get("blood_grp"));

                    obj.put("condition", document.get("condition"));

                    obj.put("problem", document.get("problem"));

                    obj.put("police_case", document.get("police_case"));

                    obj.put("is_enabled", document.get("is_enabled"));

                }

            });


            if (patientfound) {
                obj.put("status", "true");
                return String.valueOf(obj);
            }

            obj.put("status", "false");
            return String.valueOf(obj);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



        return "null";
    }





    @GET
    @Path("/get_Patient_List")
    @Produces("application/json")
    public String get_Patient_List(@QueryParam("hospital_name") String hospital_name) {

        obj = new JSONObject();
        try {

            FindIterable<org.bson.Document> iterable = collection.find(new org.bson.Document("hospital_name", hospital_name).append("is_enabled", "Yes"));

            datafound = false;
            count=0;
            iterable.forEach(new Block<org.bson.Document>() {
                @Override
                public void apply(final org.bson.Document document) {

                    ja = new JSONArray();
                    JSONObject jo = new JSONObject();

                    datafound = true;



                    jo.put("hospital_name", String.valueOf(document.get("hospital_name")));


                    jo.put("ambulance_id", String.valueOf(document.get("ambulance_id")));


                    jo.put("p_name", String.valueOf(document.get("p_name")));


                    jo.put("p_id", String.valueOf(document.get("p_id")));


                    jo.put("gender", String.valueOf(document.get("gender")));


                    jo.put("blood_grp", String.valueOf(document.get("blood_grp")));


                    jo.put("condition", String.valueOf(document.get("condition")));


                    jo.put("problem", String.valueOf(document.get("problem")));


                    jo.put("police_case", String.valueOf(document.get("police_case")));


                    jo.put("is_enabled", String.valueOf(document.get("is_enabled")));


                    ja.put(jo);

                    obj.put("Patient_"+count, ja);

                    count++;

                }
            });


            if (datafound == true) {
                obj.put("status", "true");
                obj.put("count", count);

                return String.valueOf(obj);
            }

            obj.put("status", "false");
            return String.valueOf(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "null";

    }

}
