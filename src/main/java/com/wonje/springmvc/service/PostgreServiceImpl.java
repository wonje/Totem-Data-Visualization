package com.wonje.springmvc.service;

import com.datastax.driver.core.ResultSet;
import com.wonje.springmvc.model.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wonje on 5/2/17.
 */
@Service("postgreService")
@Transactional
public class PostgreServiceImpl implements DeviceInfoService{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public DeviceInfo findByTotemDeviceAndTimeStamp(DeviceInfo deviceInfo) {
        return null;
    }

    // Getting Average Value and Save for each of totem
    public void saveAverageInfo(List<DeviceInfo> deviceInfos){
        // Calculate Watts and Store for each of totem devices
        Map lookUpTotems    = new HashMap<String, Integer>();
        List avgDeviceInfo  = new ArrayList<ArrayList<Double>>();
        Date currentTime    = new Date();
        for(DeviceInfo deviceInfo : deviceInfos){
            // Make Lookup table
            if(!lookUpTotems.containsKey(deviceInfo.getTotemDevice())) {
                lookUpTotems.put(deviceInfo.getTotemDevice(), lookUpTotems.size());
                avgDeviceInfo.add(new ArrayList<Double>());
            }

            List watts = (List) avgDeviceInfo.get((Integer) lookUpTotems.get(deviceInfo.getTotemDevice()));
            watts.add(deviceInfo.getAmp() * deviceInfo.getVolt());
        }

        for(Object totem : lookUpTotems.keySet()){
            Double tempWatt = 0.0;
            List selectedDevice = (List) avgDeviceInfo.get((Integer) lookUpTotems.get((String)totem));
            // Calculate Average Watts
            for(Object watts : selectedDevice)
                tempWatt += (Double)watts;
            // Query to PostgreSQL DB
            jdbcTemplate.update("INSERT INTO deviceInfo(totemDevice, timeStamp, watts ) VALUES (?, ?, ?,)",
                    new Object[]{(String) totem, currentTime, tempWatt / selectedDevice.size()});
        }
    }

    @Override
    public void saveDeviceInfo(DeviceInfo deviceInfo) {
        Timestamp timeStampForPostgre = new Timestamp(deviceInfo.getTimeStamp());

        String datePartition = new SimpleDateFormat("yyyy-MM-dd").format(new Date(deviceInfo.getTimeStamp()));
        jdbcTemplate.update("INSERT INTO deviceInfo(totemDevice, timeStamp, watts ) VALUES (?, ?, ?)", new Object[]{deviceInfo.getTotemDevice(),
        timeStampForPostgre, deviceInfo.getAmp() * deviceInfo.getVolt()});


//        jdbcTemplate.update("INSERT INTO deviceInfo(totemDevice, timeStamp, datePartition, date, amp, volt) VALUES (?, ?, ?, ?, ?, ?)",
//                new Object[]{deviceInfo.getTotemDevice(), timeStampForPostgre, datePartition,
//                        deviceInfo.getDate(), deviceInfo.getAmp(), deviceInfo.getVolt()});
    }

    @Override
    public List<DeviceInfo> findAllDeviceInfos(long startTime, long endTime) {
        RowMapper<DeviceInfo> mapper = new RowMapper<DeviceInfo>() {
            @Override
            public DeviceInfo mapRow(java.sql.ResultSet rs, int rowNum) throws SQLException {
                DeviceInfo deviceInfo = new DeviceInfo(rs.getString("totemdevice"), rs.getTimestamp("timestamp").getTime(),
                        rs.getString("date"), rs.getDouble("amp"), rs.getDouble("volt"));
                return deviceInfo;
            }
        };
        return jdbcTemplate.query("SELECT * FROM deviceInfo WHERE timestamp >= ? AND timestamp <= ?;",
                new Object[]{new Timestamp(startTime), new Timestamp(endTime)}, mapper);
    }
}
