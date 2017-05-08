package com.wonje.springmvc.controller;

import com.wonje.springmvc.model.DeviceInfo;
import com.wonje.springmvc.model.SchedulingState;
import com.wonje.springmvc.service.CassandraServiceImpl;
import com.wonje.springmvc.service.DeviceInfoService;
import com.wonje.springmvc.service.DeviceInfoServiceImpl;
import com.wonje.springmvc.service.PostgreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by wonje on 5/1/17.
 */
@Controller
public class TotemController {

    @Autowired
    @Qualifier("deviceInfoService")
    DeviceInfoServiceImpl deviceInfoService;
    @Autowired
    @Qualifier("cassandraService")
    CassandraServiceImpl cassandraService;
    @Autowired
    @Qualifier("postgreService")
    PostgreServiceImpl postgreServiceImpl;


    // Main
    @RequestMapping(value = "/")
    public String defaultPage(){
        return "index";
    }

    // ########## Scheduling Mode ############
    @RequestMapping(value = "/timeSchedule", params = {"minute"})
    public String schedulingChange(@RequestParam(value = "minute") int minute) {
        switch (minute){
            case 5:
            {
                SchedulingState.currentState = SchedulingState.FIVE_MIN;
                break;
            }
            case 15:
            {
                SchedulingState.currentState = SchedulingState.FIFTHTEEN_MIN;
                break;
            }
            case 30:
            {
                SchedulingState.currentState = SchedulingState.THIRTY_MIN;
                break;
            }
        }

        return "index";
    }

    // ########## Cassandra Service ##########

    // Retrieve all device info using GET method for RESTful method
    @RequestMapping(value = "/deviceInfo", method = RequestMethod.GET, params = {"startTime", "endTime"})
    @ResponseBody
    public ResponseEntity<List<DeviceInfo>> listAllDeviceInfos(@RequestParam(value = "startTime") long startTime,
                                                               @RequestParam(value = "endTime") long endTime){
        List<DeviceInfo> deviceInfos = cassandraService.findAllDeviceInfos(startTime, endTime);
//        List<DeviceInfo> deviceInfos = postgreServiceImpl.findAllDeviceInfos(startTime, endTime);

        if(deviceInfos.isEmpty()){
            System.out.println("Any deviceInfo is not found");
            return new ResponseEntity<List<DeviceInfo>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<DeviceInfo>>(deviceInfos, HttpStatus.OK);
    }



    // Upload each row of device info using POST method for RESTful method
    @RequestMapping(value = "/deviceInfo", method = RequestMethod.POST)
    public ResponseEntity<Void> createDeviceInfo(@RequestBody String line) {
        // Create DB
        cassandraService.saveDeviceInfo(new DeviceInfo(line.split("\"")[1]));
//        postgreServiceImpl.saveDeviceInfo(new DeviceInfo(line.split("\"")[1]));

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }




    // ##########PostgreSQL Service##########

//    // INSERT INTO totem.deviceInfo
//    @RequestMapping(value = "/postgre", method = RequestMethod.POST, params = {"startTime", "endTime"})
//    public String postEveryFiveMinutes(@RequestParam(value = "startTime") long startTime,
//                                       @RequestParam(value = "endTime") long endTime){
//        // TODO INSERT DB collected last 5 minutes
////        postgreServiceImpl.saveAverageInfo(deviceInfoService.findAllDeviceInfos(startTime, endTime));
//
//        // CHOICE 1 : INPUT FROM CASSANDRA
////        postgreServiceImpl.saveAverageInfo(cassandraService.findAllDeviceInfos(startTime, endTime));
//
//        // TODO CHOICE 2 : INPUT DB DIRECTLY ON THE INDEX.JSP
//
//
//
//        return "index";
//    }


    // SELECT * FROM totem.deviceInfo
    @RequestMapping(value = "/postgre", method = RequestMethod.GET)
    public ResponseEntity<List<DeviceInfo>> returnEveryFiveMinutes(){
        List<DeviceInfo> deviceInfos;
        try {
            deviceInfos = postgreServiceImpl.findAllDeviceInfos(0, new Date().getTime());
        } catch (Exception e) {
            System.out.println("Any deviceInfo is not found" + e);
            return new ResponseEntity<List<DeviceInfo>>(HttpStatus.OK);
        }

        return new ResponseEntity<List<DeviceInfo>>(deviceInfos, HttpStatus.OK);
    }
}