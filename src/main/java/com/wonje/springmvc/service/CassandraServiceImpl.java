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
    private static PreparedStatement tempStatement;
    private static BoundStatement boundStatement;
    private static BoundStatement tempBoundStatement;

    public static final String keyspace = "totem";
    public static final String table    = "deviceInfo";
    public static final String tempTable    = "tempInfo";

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
        // CREATE TABLE FOR TEMPORARY DURING 5 MINUTES
        session.execute("CREATE TABLE IF NOT EXISTS "+ keyspace +".tempInfo ( " +
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
                        + "VALUES (?,?,?,?,?,?) IF NOT EXISTS;");
        tempStatement = session.prepare(
                "INSERT INTO "+ keyspace + "." + tempTable + " (totemDevice, timeStamp, datePartition, date, amp, volt)"
                        + "VALUES (?,?,?,?,?,?) IF NOT EXISTS;");
        boundStatement = new BoundStatement(statement);
        tempBoundStatement = new BoundStatement(tempStatement);
    }

    // Return matched specific deviceinfo
    @Override
    public DeviceInfo findByTotemDeviceAndTimeStamp(DeviceInfo deviceInfo) {
        return null;
    }

    @Override
    public void saveDeviceInfo(DeviceInfo deviceInfo) {
        String datePartition = new SimpleDateFormat("yyyy-MM-dd").format(new Date(deviceInfo.getTimeStamp()));
        // INSERT INTO deviceInfo TABLE
        session.execute(boundStatement.bind(deviceInfo.getTotemDevice(), deviceInfo.getTimeStamp(), datePartition,
                deviceInfo.getDate(), deviceInfo.getAmp(), deviceInfo.getVolt()));
        // INSERT INTO tempInfo TABLE
        session.execute(tempBoundStatement.bind(deviceInfo.getTotemDevice(), deviceInfo.getTimeStamp(), datePartition,
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
        }

        return deviceInfos;
    }

    public List<DeviceInfo> findAllTempDeviceInfos(){
        ResultSet rs = session.execute("SELECT * FROM totem.tempInfo;");
        // FIXME I SHOULD RE PROGRAMMING FOR THIS PART. IT WOULD MAKE TROUBLE FOR COMPLEXITY.
        // FIXME I SHOULD USE RESTful WAY FOR EACH OF deviceInfo OBJECT USING POST METHOD.
        List<DeviceInfo> deviceInfos = new ArrayList<DeviceInfo>();
        for(Row row : rs){
            // TODO TRY TIME STRING TYPE TO STRING OR DATE REFERENCE TYPE OBJECT
            deviceInfos.add(new DeviceInfo(row.getString("totemdevice"), row.getLong("timestamp"),
                    row.getString("date"), row.getDouble("amp"), row.getDouble("volt")));
        }
        // EARASE ALL ROWS
        session.execute("TRUNCATE totem.tempInfo;");

        return deviceInfos;
    }

}
