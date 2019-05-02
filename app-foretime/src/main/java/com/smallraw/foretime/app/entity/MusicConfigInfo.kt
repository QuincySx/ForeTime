package com.smallraw.foretime.app.entity

data class MusicConfigInfo(
        /**
         * 是否播放音乐
         */
        var playMusic: Boolean = false,

        /**
         * 暂停时是否播放音乐
         */
        var restPlayMusic: Boolean = false
)