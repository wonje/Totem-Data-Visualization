package com.wonje.springmvc.configuration;

import com.wonje.springmvc.model.SchedulingState;
import com.wonje.springmvc.service.CassandraServiceImpl;
import com.wonje.springmvc.service.PostgreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by wonje on 5/7/17.
 */
@Component
public class ScheduledTasks {

    @Autowired
    @Qualifier("cassandraService")
    CassandraServiceImpl cassandraService;
    @Autowired
    @Qualifier("postgreService")
    PostgreServiceImpl postgreServiceImpl;

    @Scheduled(cron = "0 0/1 * * * *")
    public void reportFiveMinute() {
        if(SchedulingState.currentState.equals(SchedulingState.FIVE_MIN)){
            // tempInfo (Cassandra) --> getAverage() --> PostgreSQL
            // tempInfo TRUNCATE
            try {
                postgreServiceImpl.saveAverageInfo(cassandraService.findAllTempDeviceInfos());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron = "0 0/15 * * * *")
    public void reportFifthteenMinute(){
        if(SchedulingState.currentState.equals(SchedulingState.FIFTHTEEN_MIN)){
            // tempInfo (Cassandra) --> getAverage() --> PostgreSQL
            // tempInfo TRUNCATE
            postgreServiceImpl.saveAverageInfo(cassandraService.findAllTempDeviceInfos());
        }
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void reportThirtyMinute(){
        if(SchedulingState.currentState.equals(SchedulingState.THIRTY_MIN)){
            // tempInfo (Cassandra) --> getAverage() --> PostgreSQL
            // tempInfo TRUNCATE
            postgreServiceImpl.saveAverageInfo(cassandraService.findAllTempDeviceInfos());
        }
    }
}
