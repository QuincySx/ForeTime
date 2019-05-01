package com.smallraw.foretime.app.repository.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import java.util.*

@Entity(tableName = MemorialDO.TABLE_NAME)
class MemorialDO : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "description")
    var description: String? = null
    @ColumnInfo(name = "type")//0 累计日  1 倒数日
    var type: Int = 0
    @ColumnInfo(name = "color")
    var color: String? = null
    @ColumnInfo(name = "targetTime")
    var targetTime: Date? = null
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
    var repeatTime: String? = null
    @ColumnInfo(name = "createTime")
    var createTime: Date? = null
    @ColumnInfo(name = "strike")
    var isStrike: Boolean = false
    @ColumnInfo(name = "archive")
    var isArchive: Boolean = false

    constructor() {}

    @Ignore
    constructor(name: String, description: String, type: Int, color: String,
                targetTime: Date, repeatTime: String, createTime: Date) {
        this.name = name
        this.description = description
        this.type = type
        this.color = color
        this.targetTime = targetTime
        this.createTime = createTime
        this.repeatTime = repeatTime
    }

    override fun toString(): String {
        return "MemorialDO{" +
                "id=" + id +
                ", name='" + name + '\''.toString() +
                ", description='" + description + '\''.toString() +
                ", type=" + type +
                ", color='" + color + '\''.toString() +
                ", targetTime=" + targetTime +
                ", createTime=" + createTime +
                ", strike=" + isStrike +
                ", archive=" + isArchive +
                '}'.toString()
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(this.id)
        dest.writeString(this.name)
        dest.writeString(this.description)
        dest.writeInt(this.type)
        dest.writeString(this.color)
        dest.writeLong(if (this.targetTime != null) this.targetTime!!.time else -1)
        dest.writeString(this.repeatTime)
        dest.writeLong(if (this.createTime != null) this.createTime!!.time else -1)
        dest.writeByte(if (this.isStrike) 1.toByte() else 0.toByte())
        dest.writeByte(if (this.isArchive) 1.toByte() else 0.toByte())
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readValue(Long::class.java.classLoader) as Long
        this.name = `in`.readString()
        this.description = `in`.readString()
        this.type = `in`.readInt()
        this.color = `in`.readString()
        val tmpTargetTime = `in`.readLong()
        this.targetTime = if (tmpTargetTime == -1L) null else Date(tmpTargetTime)
        this.repeatTime = `in`.readString()
        val tmpCreateTime = `in`.readLong()
        this.createTime = if (tmpCreateTime == -1L) null else Date(tmpCreateTime)
        this.isStrike = `in`.readByte().toInt() != 0
        this.isArchive = `in`.readByte().toInt() != 0
    }

    companion object {
        const val TABLE_NAME = "memorial"

        @JvmField
        val CREATOR: Parcelable.Creator<MemorialDO> = object : Parcelable.Creator<MemorialDO> {
            override fun createFromParcel(source: Parcel): MemorialDO {
                return MemorialDO(source)
            }

            override fun newArray(size: Int): Array<MemorialDO?> {
                return arrayOfNulls(size)
            }
        }
    }
}
