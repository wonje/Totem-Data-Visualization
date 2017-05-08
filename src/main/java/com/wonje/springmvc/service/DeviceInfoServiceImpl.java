package com.wonje.springmvc.service;

import com.wonje.springmvc.model.DeviceInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wonje on 5/1/17.
 */
@Service("deviceInfoService")
@Transactional
public class DeviceInfoServiceImpl implements DeviceInfoService {

    private List<DeviceInfo> deviceInfos;

    @Override
    public List<DeviceInfo> findAllDeviceInfos(long startTime, long endTime) {
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
        deviceInfos.add(deviceInfo);
    }

    public void deleteAllDeviceInfos() {
        deviceInfos.clear();
    }

    public boolean isDeviceInfoExist(DeviceInfo deviceInfo) {
        return findByTotemDeviceAndTimeStamp(deviceInfo) != null;
    }
}
