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
package com.smallraw.foretime.app.entity

/**
 * @author QuincySx
 * @date 2018/7/19 上午10:48
 */
data class Weather(

    /**
     * place : 朝阳
     * parent_city : 北京
     * admin_area : 北京
     * cloud : 75
     * cond_code : 104
     * cond_txt : 阴
     * fl : 27
     * tmp : 24
     * pcpn : 0.0
     */

    var place: String? = null,
    var parent_city: String? = null,
    var admin_area: String? = null,
    var cloud: String? = null,
    var cond_code: String? = null,
    var cond_txt: String? = null,
    var fl: String? = null,
    var tmp: String? = null,
    var pcpn: String? = null
)
