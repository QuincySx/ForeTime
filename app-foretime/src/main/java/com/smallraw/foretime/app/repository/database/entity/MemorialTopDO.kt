/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.foretime.app.repository.database.entity

import androidx.room.*
import java.util.*

@Entity(
    tableName = MemorialTopDO.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MemorialDO::class,
            parentColumns = ["id"],
            childColumns = ["memorial_id"]
        )
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
    var type: Long? = null // 0 主页 1 归档页面
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
