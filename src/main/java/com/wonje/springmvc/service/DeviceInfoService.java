package com.wonje.springmvc.service;

import com.datastax.driver.core.ResultSet;
import com.wonje.springmvc.model.DeviceInfo;

import java.util.List;

/**
 * Created by wonje on 5/1/17.
 */
public interface DeviceInfoService {
    DeviceInfo findByTotemDeviceAndTimeStamp(DeviceInfo deviceInfo);

    void saveDeviceInfo(DeviceInfo deviceInfo);

    ResultSet getAllDB();

    List<DeviceInfo> findAllDeviceInfos();

    void deleteAllDeviceInfos();

    public boolean isDeviceInfoExist(DeviceInfo deviceInfo);

}
