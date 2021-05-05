package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.JsonObject;
import no.hvl.dat110.ac.restservice.AccessLog;
import no.hvl.dat110.ac.restservice.AccessCode;


import com.google.gson.Gson;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {

		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description

		post("/accessdevice/log", (req, res) -> {

			Gson gson = new Gson();
			String messJson = req.body();
			//String messString = gson.fromJson(messJson, String.class);
			int id = accesslog.add(gson.toJson(messJson));
			return new AccessEntry(id, gson.toJson(messJson));
		});

		get("/accessdevice/log", (req, res) -> {
			AccessEntry[] entries = new AccessEntry[accesslog.size()];
			for (int i = 0; i < accesslog.size(); i ++){
				entries[i] = accesslog.get(i);
			}
			return Arrays.toString(entries);
		});

		get("/accessdevice/log/:id" , (req, res) -> {
			Gson gson = new Gson();
			AccessEntry message = (accesslog.get(Integer.parseInt(req.params(":id"))));
			return message;
		});

		put("/accessdevice/code/:code", (req, res) -> {
			Gson gson = new Gson();
			String code = req.params(":code"); //[a, b] format
			String[] items = code.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
			int[] result = new int [items.length];
			for (int i = 0; i < items.length; i++){
				try {
					result[i] = Integer.parseInt(items[i]);
				} catch (NumberFormatException e) {
					System.out.println("Formatting error at doGetAccessCode! " + e);
				}
			}
			accesscode.setAccesscode(result);
			String pass = Arrays.toString(result);
			return gson.toJson(pass);
		});

		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			int [] code = accesscode.getAccesscode();
			String pass = Arrays.toString(code);
			return gson.toJson(pass);
		});

		delete("/accessdevice/log", (request, response) -> {
			accesslog.clear();
			return accesslog.toJson();
		});
    }
    
}
