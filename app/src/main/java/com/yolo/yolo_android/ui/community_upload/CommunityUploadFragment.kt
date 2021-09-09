package com.yolo.yolo_android.ui.community_upload

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentCommunityUploadBinding
import com.yolo.yolo_android.model.Document
import com.yolo.yolo_android.ui.place_list.PlaceListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityUploadFragment: BindingFragment<FragmentCommunityUploadBinding>(R.layout.fragment_community_upload) {

    companion object {
        fun newInstance() = CommunityUploadFragment()
    }

    private val viewModel: CommunityUploadViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        setFragmentResultListener(PlaceListFragment.REQ_SELECTED_PLACE) { requestKey, bundle ->
            val result = bundle.getParcelable<Document>("data")
            Log.d("aaa", "Document=$result")
            result?.let {
                viewModel.setDocument(it)
            }
        }

        viewModel.uploadResponse.observe(viewLifecycleOwner, Observer {
            Log.d("aaa", "uploadResponse=$it")
            findNavController().popBackStack()
        })

        viewModel.mBitmap.observe(viewLifecycleOwner, Observer {
            binding.ivTest.setImageBitmap(it)
        })
    }
}

class ImageSelectedDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = space
    }

}