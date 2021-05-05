package no.hvl.dat110.ac.restservice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

public class AccessLog {
	
	// atomic integer used to obtain identifiers for each access entry
	private AtomicInteger cid;
	protected ConcurrentHashMap<Integer, AccessEntry> log;
	
	public AccessLog () {
		this.log = new ConcurrentHashMap<Integer,AccessEntry>();
		cid = new AtomicInteger(0);
	}

	// TODO: add an access entry to the log for the provided message and return assigned id
	public int add(String message) {
		int id = 0;
		if (log.isEmpty()){
			log.put(1, new AccessEntry(1, message));
			id = 1;
		} else {
			int size = log.size() + 1;
			log.put(size, new AccessEntry(size, message));
		}
		return id;
	}
		
	// TODO: retrieve a specific access entry from the log
	public AccessEntry get(int id) {
		return log.get(id);
	}
	
	// TODO: clear the access entry log
	public void clear() {
		this.log.clear();
	}
	
	// TODO: return JSON representation of the access log
	public String toJson () {
		String json = null;
		Gson gson = new Gson();
		for (int i = 0; i < log.size(); i++){
			if (log.get(i) != null) {
				json += gson.toJson(log.get(i).toString());
			}
		}
    	return json;
    }

	public int size() {
		return this.log.size();
	}

}
