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
    boolean patientfound=false;

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
            collection.updateOne(new org.bson.Document("hospital_name", hospital_name).append("ambulance_id",ambulance_id), new org.bson.Document("$set", new org.bson.Document("is_enabled","No")));


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
            obj.put("p_id", p_id);
            obj.put("message", "true");
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
                obj.put("message", "true");
                return String.valueOf(obj);
            }

            obj.put("message", "false");
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
                obj.put("message", "true");
                return String.valueOf(obj);
            }

            obj.put("message", "false");
            return String.valueOf(obj);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



        return "null";
    }


}
