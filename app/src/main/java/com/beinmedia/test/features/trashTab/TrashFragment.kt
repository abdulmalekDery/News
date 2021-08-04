package com.beinmedia.test.features.trashTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.beinmedia.test.R
import com.beinmedia.test.base.BaseFragment
import com.beinmedia.test.databinding.FragmentTrashBinding
import com.beinmedia.test.models.local.models.NewsDB
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener

class TrashFragment : BaseFragment() {

    private val viewModel: TrashViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentTrashBinding
    private lateinit var trashAdapter: TrashAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrashBinding.inflate(inflater,container,false)
        binding.trashViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trashAdapter = TrashAdapter()
        binding.trashList.layoutManager = LinearLayoutManager(requireActivity())
        binding.trashList.orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
        binding.trashList.disableDragDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.UP)
        binding.trashList.disableDragDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.DOWN)
        binding.trashList.disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)
        binding.trashList.swipeListener = object : OnItemSwipeListener<NewsDB> {
            override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: NewsDB): Boolean {
                // Handle action of item swiped
                // Return false to indicate that the swiped item should be removed from the adapter's data set (default behaviour)
                // Return true to stop the swiped item from being automatically removed from the adapter's data set (in this case, it will be your responsibility to manually update the data set as necessary)
                viewModel.deletePermanently(trashAdapter.dataSet[position].id)
                if(trashAdapter.dataSet.isEmpty()){
                    binding.trashList.visibility = INVISIBLE
                    binding.theDeletedNewsListEmptyText.visibility = VISIBLE
                }
                return false
            }
        }
        binding.trashList.behindSwipedItemLayoutId = R.layout.item_behind_deleted_news
        binding.trashList.adapter = trashAdapter
        viewModel.getDeletedNewsFlow().asLiveData().observe(viewLifecycleOwner, Observer {
            deletedNews ->
            deletedNews?.let {
                if(deletedNews.isNotEmpty()) {
                    binding.trashList.visibility = VISIBLE
                    binding.theDeletedNewsListEmptyText.visibility = INVISIBLE
                    trashAdapter.dataSet = deletedNews
                    trashAdapter.notifyDataSetChanged()
                } else {
                    binding.trashList.visibility = INVISIBLE
                    binding.theDeletedNewsListEmptyText.visibility = VISIBLE
                }
            }
        })
    }

}