package io.wybis.wybisanalytics.model

import groovy.transform.Canonical
import groovy.transform.ToString
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

import javax.persistence.*

@Entity
@Table(name = "access_data")
@Canonical
@ToString(includeNames = true)
class AccessData {

    private static final long serialVersionUID = 1L

    static final String ID_KEY = "accessDataId"

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    long id

    @Column(name = "application_code")
    String applicationCode

    @Column(name = "request_path")
    String requestPath

    @Column(name = "user_id")
    String userId

    @Column(name = "session_id")
    String sessionId = '-'

    @Column(name = "request_id")
    String requestId

    @Column(name = "start_time")
    Date startTime

    @Transient
    String startTimeRaw

    @Column(name = "end_time")
    Date endTime

    @Transient
    String endTimeRaw

    @Column(name = "process_time")
    long processTime

    @Column(name = "process_time_c")
    long processTimeC

    @Column(name = "create_time")
    Date createTime

    @Column(name = "update_time")
    Date updateTime

    // persistence operations
    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date()
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date()
        this.createTime = now
        this.updateTime = now
    }

    // domain operations

    void computeStartTime(DateTimeFormatter dateTimeFap) {
        startTime = dateTimeFap.parseDateTime(startTimeRaw).toDate()
    }

    void computeStartTime(DateTimeFormatter dateTimeFap, String dateRaw) {
        String s = "$dateRaw ${this.startTimeRaw}"
        startTime = dateTimeFap.parseDateTime(s).toDate()
    }

    void computeEndTime(DateTimeFormatter dateTimeFap) {
        endTime = dateTimeFap.parseDateTime(endTimeRaw).toDate()
    }

    void computeEndTime(DateTimeFormatter dateTimeFap, String dateRaw) {
        String s = "$dateRaw ${this.endTimeRaw}"
        endTime = dateTimeFap.parseDateTime(s).toDate()
    }

    void computeProcessTimeCalculated() {
        DateTime st = new DateTime(startTime)
        DateTime et = new DateTime(endTime)
        processTimeC = (et.millis - st.millis)
    }

    String asInsertSql() {
        return "INSERT INTO access_data ( application_code, request_path, user_id, request_id, session_id, start_time, end_time, process_time, process_time_c, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
    }

    List asInsertSqlValues() {
        return [this.applicationCode, this.requestPath, this.userId, this.requestId, this.sessionId, this.startTime, this.endTime, this.processTime, this.processTimeC, this.createTime, this.updateTime]
    }

    String asUpdateSql() {
        return "UPDATE access_data SET application_code = ?, request_path = ?, user_id = ?, session_id = ?, start_time = ?, end_time = ?, process_time = ?, process_time_c = ?, create_time = ?, update_time = ? WHERE request_id = ?;"
    }

    List asUpdateSqlValues() {
        return [this.applicationCode, this.requestPath, this.userId, this.sessionId, this.startTime, this.endTime, this.processTime, this.processTimeC, this.createTime, this.updateTime, this.requestId]
    }

    String asCSV(DateTimeFormatter dateTimeFat) {
        DateTime dateTime = new DateTime(this.startTime)
        String startTimeS = dateTime.toString(dateTimeFat)
        dateTime = new DateTime(this.endTime)
        String endTimeS = dateTime.toString(dateTimeFat)
        dateTime = new DateTime(this.createTime)
        String createTimeS = dateTime.toString(dateTimeFat)
        dateTime = new DateTime(this.updateTime)
        String updateTimeS = dateTime.toString(dateTimeFat)
        return "'${this.id}','${this.appId}','${this.path}','${this.userId}','${this.sessionId}','${this.requestId}','$startTimeS','$endTimeS',${this.processTime},'$createTimeS','$updateTimeS'"
    }
}
