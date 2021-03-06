/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package dev.hitools.common.widget.dialog.select


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import dev.hitools.common.BR
import dev.hitools.common.R
import dev.hitools.common.adapter.BindAdapter
import dev.hitools.common.databinding.DialogUnitSelectBinding
import dev.hitools.common.extensions.binding
import dev.hitools.common.extensions.dp2px
import dev.hitools.common.widget.dialog.BaseDialog
import dev.hitools.common.widget.recyclerview.itemdecoration.ColorDecoration

/**
 * Created by yuhaiyang on 2016/10/31.
 * 一个统一的从底下弹出的Dialog选择
 */

class SelectDialog<T>(context: Context) : BaseDialog(context, R.style.Theme_Dialog_Bottom_Transparent) {
    private var selectBlock: ((T) -> Unit)? = null

    val adapter = BindAdapter<T>()
    var data: MutableList<T>?
        get() = adapter.data
        set(data) {
            adapter.data = data
        }
    var maxHeight = 300.dp2px()

    @Suppress("MemberVisibilityCanBePrivate")
    var itemDecoration: RecyclerView.ItemDecoration = ColorDecoration(context)

    private val binding: DialogUnitSelectBinding by binding()

    init {
        adapter.addLayout(BR.itemUnitSelect, R.layout.item_dialog_selet)
        adapter.setOnItemClickListener {
            notifySelectCityChanged(adapter.getItem(it))
            dismiss()
        }
        fromBottom(bottom = true)
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DialogUnitSelectBinding.inflate(LayoutInflater.from(context))
        binding.list.maxHeight = maxHeight
        binding.list.addItemDecoration(itemDecoration)
        binding.list.adapter = adapter
    }

    fun addLayout(variableId: Int, layoutRes: Int, viewType: Int = 0) {
        adapter.addLayout(variableId, layoutRes, viewType)
    }

    private fun notifySelectCityChanged(selected: T) {
        selectBlock?.let { it(selected) }
    }

    fun setOnSelectedListener(listener: ((T) -> Unit)?) {
        selectBlock = listener
    }

    interface OnSelectedListener<T> {
        fun onSelected(t: T)
    }
}
