package com.wonje.springmvc.service;

import com.datastax.driver.core.ResultSet;
import com.wonje.springmvc.model.DeviceInfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by wonje on 5/1/17.
 */
@Service("deviceInfoService")
@Transactional
public class DeviceInfoServiceImpl implements DeviceInfoService {


    public static final AtomicLong counter = new AtomicLong();

    private static List<DeviceInfo> deviceInfos;

    @Override
    public List<DeviceInfo> findAllDeviceInfos() {
        return deviceInfos;
    }

    // Get exactly matched device
    @Override
    public DeviceInfo findByTotemDeviceAndTimeStamp(DeviceInfo device) {
        for(DeviceInfo deviceInfo : deviceInfos){
            if(deviceInfo.getTotemDevice().equalsIgnoreCase(device.getTotemDevice()) &&
                    deviceInfo.getTimeStamp() == device.getTimeStamp())
                return deviceInfo;
            }
        return null;
    }

    @Override
    public void saveDeviceInfo(DeviceInfo deviceInfo) {
//        deviceInfo.setId(counter.incrementAndGet());
        deviceInfos.add(deviceInfo);
    }

    @Override
    public ResultSet getAllDB() {
        return null;
    }

    @Override
    public void deleteAllDeviceInfos() {
        deviceInfos.clear();
        counter.set(0);
    }

    @Override
    public boolean isDeviceInfoExist(DeviceInfo deviceInfo) {
        return findByTotemDeviceAndTimeStamp(deviceInfo) != null;
    }
}
