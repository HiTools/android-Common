package dev.hitools.noah.modules.sample.detail.utils.file.save

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FFileSaveBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment


/**
 * Created by xxx on 2020-09-21.
 */
class SampleFileSaveFragment : AppBindFragment<FFileSaveBinding, SampleFileSaveViewModel>() {

    override fun getLayout(): Int = R.layout.f_file_save


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.save.setOnClickListener { saveFile()}
    }


    private fun saveFile() {
        val fileName = "a.jpg"
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.splash)

        val contentValue = ContentValues()
        contentValue.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValue.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/aaaaa")
        contentValue.put(MediaStore.MediaColumns.MIME_TYPE, "images/*")

        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValue)
        if(uri == null) {
            Log.i("yhy", "uri is null")
            return
        }

        val outputStream = requireActivity().contentResolver.openOutputStream(uri)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        outputStream?.flush()
        outputStream?.close()
    }
}