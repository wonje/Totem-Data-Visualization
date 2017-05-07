package com.wonje.springmvc.service;

import com.datastax.driver.core.*;
import com.datastax.driver.extras.codecs.date.SimpleDateCodec;
import com.datastax.driver.extras.codecs.date.SimpleTimestampCodec;
import com.wonje.springmvc.model.DeviceInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

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

        // CREATE KEYSPACE totem
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + keyspace + " WITH REPLICATION = { 'class' : 'SimpleStrategy', " +
                "'replication_factor' : 1 };");

        session.execute("USE " + keyspace + ";");

        // CREATE TABLE
        session.execute("CREATE TABLE IF NOT EXISTS "+ keyspace +".deviceInfo ( " +
                "    totemDevice text, " +
                "    timeStamp timestamp, " +
                "    datePartition text, " +
                "    date text, " +
                "    amp double, " +
                "    volt double, " +
                "    PRIMARY KEY (datePartition, timeStamp)" +
                ");");

        statement = session.prepare(
                "INSERT INTO "+ keyspace + "." + table + " (totemDevice, timeStamp, datePartition, date, amp, volt)"
                        + "VALUES (?,?,?,?,?,?);");
        boundStatement = new BoundStatement(statement);
    }

    // Return matched specific deviceinfo
    @Override
    public DeviceInfo findByTotemDeviceAndTimeStamp(DeviceInfo deviceInfo) {
        return null;
    }

    @Override
    public void saveDeviceInfo(DeviceInfo deviceInfo) {
        String datePartition = new SimpleDateFormat("yyyy-MM-dd").format(new Date(deviceInfo.getTimeStamp()));
        session.execute(boundStatement.bind(deviceInfo.getTotemDevice(), deviceInfo.getTimeStamp(), datePartition,
                deviceInfo.getDate(), deviceInfo.getAmp(), deviceInfo.getVolt()));
        System.out.println(deviceInfo);
    }

    @Override
    public List<DeviceInfo> findAllDeviceInfos(long startTime, long endTime) {
        ResultSet rs = session.execute("SELECT * FROM totem.deviceInfo WHERE timestamp >= " + startTime
                + " AND timestamp <= "+ endTime +" ALLOW FILTERING;");
        List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
        for(Row row : rs){
            // TODO TRY TIME STRING TYPE TO STRING OR DATE REFERENCE TYPE OBJECT
            deviceInfos.add(new DeviceInfo(row.getString("totemdevice"), row.getLong("timestamp"),
                    row.getString("date"), row.getDouble("amp"), row.getDouble("volt")));
            System.out.println(deviceInfos.get(0));
        }

        return deviceInfos;
    }

}
