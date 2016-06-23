package info.bluefoot.winter.service;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KeepUtilAliveScheduler {
    private static final String[] URLS = new String[] {"http://util-gewton.rhcloud.com", "http://journal-gewton.rhcloud.com" };

    private static Logger logger = LoggerFactory.getLogger(KeepUtilAliveScheduler.class);
    @Scheduled(fixedRate=10800000)
    public void keepWinterAlive() {
        HttpClient client = HttpClientBuilder.create().build();
        for(String url : URLS) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = null;
            String responseString = null;
            try {
                response = client.execute(request);
                responseString = new BasicResponseHandler().handleResponse(response);
            } catch (Exception e) {
                logger.error("Error keeping " + url + " alive", e);
                return;
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==HttpStatus.SC_OK) {
                logger.info("Successfully kept " + url + " alive");
            } else {
                logger.error("Got HTTP status code " + statusCode + " while keeping " + url + " alive. Response: \n" + responseString);
            }
        }
    }
}
