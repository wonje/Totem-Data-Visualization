package com.wonje.springmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wonje on 5/4/17.
 */
public class DataProcessor {
    public static final String REST_SERVICE_URI = "http://localhost:8088";
    private BufferedReader br;

    public DataProcessor() throws URISyntaxException, FileNotFoundException {
        URL dataResource = getClass().getClassLoader().getResource("grid_stats.csv");
        br = new BufferedReader(new FileReader(dataResource.toURI().toString().split(":", 2)[1]));
    }

    public boolean postData() throws IOException {
        String line;
        // READ NEXT LINE
        if((line = br.readLine()) == null)
            return false;

        // TODO Post Data to http://localhost:8088/
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getMessageConverters());
        restTemplate.postForObject(REST_SERVICE_URI+"/deviceInfo", line, String.class);

        return true;
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters =
                new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }

}
