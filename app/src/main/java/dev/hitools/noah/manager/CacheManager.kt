package dev.hitools.noah.manager

/**
 * Created by yuhaiyang on 2018/8/8.
 * 缓存管理
 */
class CacheManager private constructor() {

    fun clearCache() {
        // TODO
    }

    companion object {

        /**
         * 这个东西使用后可以被回收
         */
        @Volatile
        private var sInstance: CacheManager? = null

        val instance: CacheManager
            get() = sInstance ?: synchronized(CacheManager::class.java) {
                sInstance ?: CacheManager().also { sInstance = it }
            }
    }
}
