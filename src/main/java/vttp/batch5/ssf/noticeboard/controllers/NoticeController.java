package vttp.batch5.ssf.noticeboard.controllers;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

// Use this class to write your request handlers

@Controller
@RequestMapping
public class NoticeController {

    @Autowired
    private NoticeService noticeSvc;

    // task 1
    @GetMapping(path = { "/", "/index.html" })
    public String getLanding(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    // task 2
    @PostMapping(path = "/notice")
    public String postNotice(@Valid @ModelAttribute Notice notice, BindingResult bindings,
            Model model) {

        if (bindings.hasErrors()) {
            return "notice";
        }

        // validate categories -- must include at least 1 category
        if (notice.getCategories() == null || notice.getCategories().size() <= 0) {
            FieldError err = new FieldError("notice", "categories", "Must include at least 1 category");
            bindings.addError(err);
            return "notice";
        }

        // validate post date -- date is mandatory
        Date postDate = notice.getPostDate();
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // String postDateString = sdf.format(postDate);
        if (postDate == null) {
            FieldError err = new FieldError("notice", "postDate", "Post Date is mandatory");
            bindings.addError(err);
            return "notice";
        }

        System.out.println(notice.toString());

        // task 5
        JsonObject json = notice.toJson();
        System.out.println(json.toString());
        String resp = noticeSvc.postToNoticeServer(json);

        JsonReader reader = Json.createReader(new StringReader(resp));
        JsonObject obj = reader.readObject();

        if (obj.getString("id") == null || obj.getString("id").isEmpty()){
            String message = obj.getString("message");
            model.addAttribute("message", message);
            return "view3";
        }

        // noticeSvc.insertNotices(obj);
        String id = obj.getString("id");
        model.addAttribute("id", id);
        return "view2";

    }

    // task 6
    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getHealth(){
        String key = noticeSvc.getRandomKey();

        if (key == null  || key.isEmpty()){
            return ResponseEntity.status(503)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");
        } 

        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");
    }

    // @PostMapping(path = "/notice", produces = MediaType.APPLICATION_JSON_VALUE,
    // consumes = MediaType.APPLICATION_JSON_VALUE)
    // @ResponseBody
    // public ResponseEntity<String> postNotice(HttpSession sess){

    // Notice notice = (Notice) sess.getAttribute("notice");

    // RequestEntity<String> req = noticeSvc.postToNoticeServer(notice);

    // RestTemplate template = new RestTemplate();

    // try{
    // ResponseEntity<String> resp = template.exchange(req, String.class);
    // String payload = resp.getBody();
    // System.out.printf(">>> payload: %s\n", payload);

    // JsonReader reader = Json.createReader(new StringReader(payload));
    // JsonObject jsonObj = reader.readObject();

    // return ResponseEntity.ok(jsonObj.toString());

    // } catch (Exception ex){
    // ResponseEntity<String> resp = template.exchange(req, String.class);
    // String payload = resp.getBody();
    // System.out.printf(">>> payload: %s\n", payload);

    // JsonReader reader = Json.createReader(new StringReader(payload));
    // JsonObject jsonObj = reader.readObject();

    // return ResponseEntity.status(400)
    // .body(jsonObj.toString());
    // }

}
