package com.wiatec.boblive.pojo;

/**
 * Created by xuchengpeng on 23/06/2017.
 */
public class AuthorizationInfo {

    private int id;
    private String key;
    private String mac;
    private String group;
    private short active;
    private String activeDate;
    private long activeTime;
    private short level;
    private String memberDate;
    private long memberTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public short getActive() {
        return active;
    }

    public void setActive(short active) {
        this.active = active;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public String getMemberDate() {
        return memberDate;
    }

    public void setMemberDate(String memberDate) {
        this.memberDate = memberDate;
    }

    public long getMemberTime() {
        return memberTime;
    }

    public void setMemberTime(long memberTime) {
        this.memberTime = memberTime;
    }

    @Override
    public String toString() {
        return "AuthorizationInfo{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", mac='" + mac + '\'' +
                ", group='" + group + '\'' +
                ", active=" + active +
                ", activeDate='" + activeDate + '\'' +
                ", activeTime=" + activeTime +
                ", level=" + level +
                ", memberDate='" + memberDate + '\'' +
                ", memberTime=" + memberTime +
                '}';
    }
}
