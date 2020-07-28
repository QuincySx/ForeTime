package com.smallraw.foretime.app.repository.database.entity

import androidx.room.*
import java.util.*

@Entity(tableName = MemorialTopDO.TABLE_NAME,
        foreignKeys = [
            ForeignKey(entity = MemorialDO::class,
                    parentColumns = ["id"],
                    childColumns = ["memorial_id"])
        ],
        indices = [
            Index(value = ["memorial_id"], unique = true)
        ]
)
class MemorialTopDO {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
    @ColumnInfo(name = "memorial_id")
    var memorial_id: Long? = null
    @ColumnInfo(name = "type")
    var type: Long? = null//0 主页 1 归档页面
    @ColumnInfo(name = "createTime")
    var createTime: Date? = null

    constructor() {}

    @Ignore
    constructor(memorial_id: Long?) {
        this.memorial_id = memorial_id
        this.createTime = Date()
    }

    @Ignore
    constructor(memorial_id: Long?, type: Long?) {
        this.memorial_id = memorial_id
        this.createTime = Date()
        this.type = type
    }

    companion object {
        const val TABLE_NAME = "memorial_top"
    }
}
