package utils;

import java.time.LocalDateTime;
import org.json.JSONObject;

import constants.Constants;
import tables.LogEvent;

public class LogEventFactory {
    public static LogEvent createRobotFromJson(JSONObject data) {
        int logId = GenerateId.getLastId(Constants.DEFAULT_LOG_EVENT_TABLE_NAME) + 1;
        int robotId = data.getInt("robotId");
        LocalDateTime timeStamp = LocalDateTime.now();
        int avenue = data.getInt("avenue");
        int street = data.getInt("street");
        int sirens = data.getInt("sirens");

        return new LogEvent(logId,robotId, timeStamp, avenue, street, sirens);
    }
}