package dev.hitools.noah.modules.sample.main

import dev.hitools.noah.entries.http.AppPageResponse
import dev.hitools.noah.manager.RetrofitManager
import dev.hitools.noah.modules.base.mvvm.model.AppBaseModel
import dev.hitools.noah.modules.sample.entries.SampleTestPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SampleModel : AppBaseModel() {
    private val httpService = RetrofitManager.instance.appService

    /**
     * testPage
     */
    suspend fun testPage(page: Int): AppPageResponse<SampleTestPage> = withContext(Dispatchers.IO) {
        return@withContext httpService.testPage(page, 20)
    }

}