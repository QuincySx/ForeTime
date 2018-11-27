package com.smallraw.foretime.app.repository.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = MemorialEntity.TABLE_NAME)
public class MemorialEntity implements Parcelable {
    public final static String TABLE_NAME = "memorial";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "type")//0 累计日  1 倒数日
    private int type;
    @ColumnInfo(name = "color")
    private String color;
    @ColumnInfo(name = "targetTime")
    private Date targetTime;
    /**
     * y 年
     * M 月
     * d 日
     * h 时 在上午或下午 (1~12)
     * H 时 在一天中 (0~23)
     * m 分
     * E 星期
     */
    @ColumnInfo(name = "repeatTime")
    private String repeatTime;
    @ColumnInfo(name = "createTime")
    private Date createTime;
    @ColumnInfo(name = "strike")
    private boolean strike;
    @ColumnInfo(name = "archive")
    private boolean archive;

    public MemorialEntity() {
    }

    @Ignore
    public MemorialEntity(String name, String description, int type, String color,
                          Date targetTime, String repeatTime, Date createTime) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.color = color;
        this.targetTime = targetTime;
        this.createTime = createTime;
        this.repeatTime = repeatTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(Date targetTime) {
        this.targetTime = targetTime;
    }

    public String getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(String repeatTime) {
        this.repeatTime = repeatTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isStrike() {
        return strike;
    }

    public void setStrike(boolean strike) {
        this.strike = strike;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    @Override
    public String toString() {
        return "MemorialEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", color='" + color + '\'' +
                ", targetTime=" + targetTime +
                ", createTime=" + createTime +
                ", strike=" + strike +
                ", archive=" + archive +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.type);
        dest.writeString(this.color);
        dest.writeLong(this.targetTime != null ? this.targetTime.getTime() : -1);
        dest.writeString(this.repeatTime);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
        dest.writeByte(this.strike ? (byte) 1 : (byte) 0);
        dest.writeByte(this.archive ? (byte) 1 : (byte) 0);
    }

    protected MemorialEntity(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.type = in.readInt();
        this.color = in.readString();
        long tmpTargetTime = in.readLong();
        this.targetTime = tmpTargetTime == -1 ? null : new Date(tmpTargetTime);
        this.repeatTime = in.readString();
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        this.strike = in.readByte() != 0;
        this.archive = in.readByte() != 0;
    }

    public static final Creator<MemorialEntity> CREATOR = new Creator<MemorialEntity>() {
        @Override
        public MemorialEntity createFromParcel(Parcel source) {
            return new MemorialEntity(source);
        }

        @Override
        public MemorialEntity[] newArray(int size) {
            return new MemorialEntity[size];
        }
    };
}
