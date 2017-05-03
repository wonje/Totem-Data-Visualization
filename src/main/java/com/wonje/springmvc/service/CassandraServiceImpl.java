package com.wonje.springmvc.service;

import com.datastax.driver.core.*;
import com.datastax.driver.extras.codecs.date.SimpleDateCodec;
import com.datastax.driver.extras.codecs.date.SimpleTimestampCodec;
import com.wonje.springmvc.model.DeviceInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wonje on 5/2/17.
 */
@Service("cassandraService")
@Transactional
public class CassandraServiceImpl implements DeviceInfoService {
    private static Cluster cluster;
    private static Session session;
    private static PreparedStatement statement;
    private static BoundStatement boundStatement;

    public static final String keyspace = "totem";
    public static final String table    = "deviceInfo";

    private static Cluster connect(String node) {
        return Cluster.builder().addContactPoint(node).build();
    }

    static {
        cluster = connect("localhost");
        cluster.getConfiguration().getCodecRegistry().register(SimpleTimestampCodec.instance, SimpleDateCodec.instance);
        session = cluster.connect();

        session.execute("USE " + keyspace + ";");

        statement = session.prepare(
                "INSERT INTO "+ keyspace + "." + table + " (totemDevice, date, timeStamp, amp, volt)"
                        + "VALUES (?,?,?,?,?);");
        boundStatement = new BoundStatement(statement);
    }

    // Return matched specific deviceinfo
    @Override
    public DeviceInfo findByTotemDeviceAndTimeStamp(DeviceInfo deviceInfo) {
        return null;
    }

    @Override
    public void saveDeviceInfo(DeviceInfo deviceInfo) {
        session.execute("USE " + keyspace + ";"); //?
        session.execute(boundStatement.bind(deviceInfo.getTotemDevice(), deviceInfo.getTimeStamp(), deviceInfo.getDate(),
                deviceInfo.getAmp(), deviceInfo.getVolt()));
    }

    @Override
    public ResultSet getAllDB() {
        ResultSet rs = session.execute("SELECT * FROM totem.deviceInfo;");
        return rs;
    }

    @Override
    public List<DeviceInfo> findAllDeviceInfos() {
        ResultSet rs = session.execute("SELECT * FROM totem.deviceInfo;");
        List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
        for(Row row : rs){
            deviceInfos.add(new DeviceInfo(row.getString("totemdevice"), row.getLong("timestamp"),
                    row.getString("date"), row.getDouble("amp"), row.getDouble("volt")));
            System.out.println(deviceInfos.get(0));
        }

        // if only one row, then rs.one() == row


        return deviceInfos;
    }

    @Override
    public void deleteAllDeviceInfos() {
        // RESET DATABASE
        session.execute("DROP KEYSPACE totem;");
    }

    @Override
    public boolean isDeviceInfoExist(DeviceInfo deviceInfo) {
        return false;
    }
}
