package com.wonje.springmvc;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.extras.codecs.date.SimpleDateCodec;
import com.datastax.driver.extras.codecs.date.SimpleTimestampCodec;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by wonje on 5/4/17.
 */
public class TotemDataUploadAgent {

    public static void main(String[] args) throws IOException, URISyntaxException {
        DataProcessor dataProcessor = new DataProcessor();

        // TODO Post Each row of data
        System.out.println("Data Processing...");
        while(dataProcessor.postData());
//        for(int i = 0; i < 100000; i++)
            dataProcessor.postData();
        System.out.println("Data Processing is done.");
    }
}
