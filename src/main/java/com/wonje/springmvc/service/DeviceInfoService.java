package com.wonje.springmvc.service;

import com.wonje.springmvc.model.DeviceInfo;

import java.util.List;

/**
 * Created by wonje on 5/1/17.
 */
public interface DeviceInfoService {
    DeviceInfo findByTotemDeviceAndTimeStamp(DeviceInfo deviceInfo);

    void saveDeviceInfo(DeviceInfo deviceInfo);

    List<DeviceInfo> findAllDeviceInfos(long startTime, long endTime);

}
