package com.wonje.springmvc.controller;

import com.wonje.springmvc.model.DeviceInfo;
import com.wonje.springmvc.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by wonje on 5/1/17.
 */
@Controller
//@RestController
public class TotemController {
    // SERVICE FOR RETRIEVAL / MANIPULATION WORK OF ALL DATA
    @Autowired
    DeviceInfoService cassandraService;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    // TODO Display All data set
    @RequestMapping(value = "/")
    public String displayData(ModelMap model){
        // TODO Set Attributes
//        model.addAttribute("deviceInfos",deviceInfos);

        return "index";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String forwardToHello(ModelMap model){
        model.addAttribute("message", "Hello Spring Frame MVC!");
        return "hello";
    }

    // TODO Retrieve all device info using GET method for RESTful method
    @RequestMapping(value = "/deviceInfo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<DeviceInfo>> listAllDeviceInfos() {
        List<DeviceInfo> deviceInfos = cassandraService.findAllDeviceInfos();
        if(deviceInfos.isEmpty()){
            System.out.println("Any deviceInfo is not found");
            return new ResponseEntity<List<DeviceInfo>>(HttpStatus.NO_CONTENT);
        }
        // TODO Return from Cassandra

        return new ResponseEntity<List<DeviceInfo>>(deviceInfos, HttpStatus.OK);
    }

    // TODO Upload each row of device info using POST method for RESTful method
    @RequestMapping(value = "/deviceInfo", method = RequestMethod.POST)
//    @ResponseBody
    public ResponseEntity<Void> createDeviceInfo(@RequestBody String line) {
        // TODO Create DB
        cassandraService.saveDeviceInfo(new DeviceInfo(line.split("\"")[1]));

        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/deviceInfo/{id}").buildAndExpand(deviceInfo.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    // DELETE ALL DEVICE INFOS ##############################################################
    @RequestMapping(value = "/deviceInfo", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<DeviceInfo> deleteAllDeviceInfos() {
//        deviceInfoService.deleteAllDeviceInfos();
        return new ResponseEntity<DeviceInfo>(HttpStatus.NO_CONTENT);
    }
}