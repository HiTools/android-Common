package dev.hitools.noah.modules.sample.detail.utils.json

import android.os.Bundle
import android.view.View
import dev.hitools.common.extensions.parseJSON
import dev.hitools.common.extensions.toJSON
import dev.hitools.common.widget.PrintView
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FSampleJsonBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2020-01-17.
 */
class SampleJsonFragment : AppBindFragment<FSampleJsonBinding, SampleJsonViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_json

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrintView.init(binding.printView)

        val test = TestJSON("张三", 10)

        val testMap = HashMap<String, TestJSON>()
        testMap["aaa"] = test

        var json: String? = null
        binding.toJson.setOnClickListener {
            PrintView.reset()
            json = testMap.toJSON()
            PrintView.print("json = $json")
        }

        binding.fromJson.setOnClickListener {
            val result: HashMap<String, TestJSON>? = json?.parseJSON()
            PrintView.reset()
            PrintView.print(result?.toString())
        }
    }


    class TestJSON(
        var name: String? = null,
        var age: Int = 0
    ) {
        override fun toString(): String {
            return "TestJSON(name=$name, age=$age)"
        }
    }


}