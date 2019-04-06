package hello;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Path("/hello")
public class HelloWorldEndPoint {
    private ExecutorService executor = Executors.newCachedThreadPool();

    @GET
    @Path("/world")
    public String test() {
        return "Hello world!";
    }

    @GET
    @Path("/sse")
    public ResponseEntity<SseEmitter> handleSseInEntity() {
        SseEmitter emitter = new SseEmitter();
        executor.execute(() -> {
            try {
                emitter.send("/sseInResponseEntity" + " @ " + new Date());
                // we could send more events
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setAccessControlAllowOrigin("*");
        ResponseEntity<SseEmitter> entity = new ResponseEntity<>(emitter, headers, HttpStatus.OK);
        return entity;
    }

}
