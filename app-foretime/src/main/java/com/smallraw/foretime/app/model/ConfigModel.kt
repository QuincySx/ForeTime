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

import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.repository.database.entity.ConfigDO
import java.util.Date

public class ConfigModel {
    companion object {
        @JvmStatic
        public fun set(key: String, value: String) {
            set(key, value, 1000 * 60 * 60 * 2)
        }

        @JvmStatic
        public fun set(key: String, value: String, time: Long) {
            val configDao = App.getInstance().getDatabase().configDao()

            val findByKey = configDao.findByKey(key)
            if (findByKey != null) {
                findByKey.value = value
                findByKey.createTime = Date()
                findByKey.overTime = time
                configDao.update(findByKey)
            } else {
                val configEntity = ConfigDO()
                configEntity.name = key
                configEntity.value = value
                configEntity.createTime = Date()
                configEntity.overTime = time
                configDao.insert(configEntity)
            }
        }

        @JvmStatic
        public fun get(key: String): String {
            return get(key, true)
        }

        @JvmStatic
        public fun get(key: String, checkTimeout: Boolean): String {
            val configDao = App.getInstance().getDatabase().configDao()

            val findByKey = configDao.findByKey(key)
            if (findByKey != null) {
                if (!checkTimeout) {
                    return findByKey.value!!
                }
                val time = findByKey.createTime?.time?.plus(findByKey.overTime!!)
                val nowTime = Date().time
                if (nowTime < time!!) {
                    return findByKey.value!!
                }
            }
            return ""
        }
    }
}
