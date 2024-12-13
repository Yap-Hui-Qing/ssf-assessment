package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	private final Logger logger = Logger.getLogger(NoticeService.class.getName());

	@Value("${noticeboard.db.server.host}")
	private String server;

	@Autowired
	private NoticeRepository noticeRepo;

	// TODO: Task 3
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type
	public String postToNoticeServer(JsonObject json) {

		System.out.printf(">>> request payload: %s\n", json.toString());		

        // create a request
        RequestEntity<String> req = RequestEntity
            .post(server + "notice")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(json.toString(), String.class);

		System.out.println(server + "notice");
		RestTemplate template = new RestTemplate();

		try{
            ResponseEntity<String> resp = template.exchange(req, String.class);
			String payload = resp.getBody();
            System.out.printf(">>> payload: %s\n", payload);
            
			JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject jsonObj = reader.readObject();

			// write the success json payload into redis
			noticeRepo.insertNotices(jsonObj);
			// String id = noticeRepo.insertNotices(jsonObj);
			// System.out.printf(">>> id: ");
            return payload;

		} catch (HttpClientErrorException ex){
			System.out.println(ex.getResponseBodyAsString());
            return ex.getResponseBodyAsString();
		}

	}

	// // write the success json payload into redis
	// public void insertNotices(JsonObject obj){
	// 	noticeRepo.insertNotices(obj);
	// }

	// get random key
	public String getRandomKey(){
		return noticeRepo.getRandomKey();
	}

}
