package com.smallraw.time.model


import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.repository.database.entity.ConfigDO
import java.util.*

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

