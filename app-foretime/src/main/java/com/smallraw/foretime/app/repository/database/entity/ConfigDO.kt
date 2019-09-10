package com.smallraw.foretime.app.repository.db.entity

import androidx.room.*
import java.util.*

@Entity(tableName = ConfigDO.TABLE_NAME,
        indices = [
            Index(value = ["name"], unique = true)
        ]
)
class ConfigDO {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "value")
    var value: String? = null
    @ColumnInfo(name = "overTime")
    var overTime: Long? = null
    @ColumnInfo(name = "createTime")
    var createTime: Date? = null

    constructor() {}

    @Ignore
    constructor(name: String, value: String, overTime: Long?) {
        this.name = name
        this.value = value
        this.overTime = overTime
        this.createTime = Date()
    }

    @Ignore
    constructor(name: String, value: String) {
        this.name = name
        this.value = value
        this.overTime = null
        this.createTime = Date()
    }

    @Ignore
    constructor(id: Long?, name: String, value: String, overTime: Long?, createTime: Date) {
        this.id = id
        this.name = name
        this.value = value
        this.overTime = overTime
        this.createTime = createTime
    }

    override fun toString(): String {
        return "ConfigDO{" +
                "id=" + id +
                ", name='" + name + '\''.toString() +
                ", value='" + value + '\''.toString() +
                ", overTime=" + overTime +
                ", createTime=" + createTime +
                '}'.toString()
    }

    companion object {
        const val TABLE_NAME = "config"
    }
}
