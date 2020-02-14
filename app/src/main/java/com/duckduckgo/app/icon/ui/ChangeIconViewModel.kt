/*
 * Copyright (c) 2020 DuckDuckGo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duckduckgo.app.icon.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.duckduckgo.app.icon.api.AppIcon
import com.duckduckgo.app.icon.api.IconModifier
import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.app.statistics.pixels.Pixel
import javax.inject.Inject

class ChangeIconViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val appIconModifier: IconModifier,
    private val pixel: Pixel
) : ViewModel() {

    data class IconViewData(val appIcon: AppIcon, val selected: Boolean) {
        companion object {
            fun from(appIcon: AppIcon, selectedAppIcon: AppIcon): IconViewData {
                return if (appIcon.componentName == selectedAppIcon.componentName) {
                    IconViewData(appIcon, true)
                } else {
                    IconViewData(appIcon, false)
                }
            }
        }
    }

    data class ViewState(
        val appIcons: List<IconViewData>
    )

    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    init {
        pixel.fire(Pixel.PixelName.APP_ICON_OPENED)
    }

    fun start() {
        val selectedIcon = settingsDataStore.appIcon
        viewState.value = ViewState(AppIcon.values().map { IconViewData.from(it, selectedIcon) })
    }

    private fun currentViewState(): ViewState {
        return viewState.value!!
    }
}