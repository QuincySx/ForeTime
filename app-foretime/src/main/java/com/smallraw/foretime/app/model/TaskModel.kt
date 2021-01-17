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
package com.smallraw.foretime.app.model

import android.app.Application
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
import com.smallraw.foretime.app.repository.database.entity.MemorialTopDO

fun thoroughDeleteTask(app: Application, memorialDO: MemorialDO) {
    memorialDO.isStrike = true
    (app as App).getRepository().delete(memorialDO.id!!)
}

fun thoroughDeleteTaskAll(app: Application, memorialDOS: List<MemorialDO>) {
    for (item in memorialDOS) {
        (app as App).getRepository().deleteTopTaskAll(item.id!!)
    }
    (app as App).getRepository().deleteTask(memorialDOS)
}

fun deleteTask(app: Application, memorialDO: MemorialDO) {
    memorialDO.isStrike = true
    (app as App).getRepository().update(memorialDO)
}

fun deleteTaskAll(app: Application, memorialDOS: List<MemorialDO>) {
    for (item in memorialDOS) {
        item.isStrike = true
    }
    (app as App).getRepository().update(memorialDOS)
}

fun unDeleteTask(app: Application, memorialDO: MemorialDO) {
    memorialDO.isStrike = false
    (app as App).getRepository().update(memorialDO)
}

fun archivingTask(app: Application, memorialDO: MemorialDO) {
    memorialDO.isArchive = true
    (app as App).getRepository().update(memorialDO)
}

fun unArchivingTask(app: Application, memorialDO: MemorialDO) {
    memorialDO.isArchive = false
    (app as App).getRepository().update(memorialDO)
}

fun isTopTask(app: Application, memorialDO: MemorialDO, type: Long): Boolean {
    return (app as App).getRepository().isTopTask(memorialDO.id!!, type)
}

fun topTask(app: Application, memorialDO: MemorialDO, type: Long) {
    val memorialTopEntity = MemorialTopDO(memorialDO.id, type)
//    (app as App).getRepository().deleteTopTask(memorialDO.id)
    (app as App).getRepository().insertTopTask(memorialTopEntity)
}

fun unTopTask(app: Application, memorialDO: MemorialDO, type: Long) {
    (app as App).getRepository().deleteTopTask(memorialDO.id!!, type)
}
