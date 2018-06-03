package com.aquaowlet.myreward.taskdetails

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.databinding.library.baseAdapters.BR
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.databinding.ActivityTaskDetailsBinding
import com.aquaowlet.myreward.util.Constant

/**
 * Display task details.
 */
class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.title_task_details)

        binding = DataBindingUtil.setContentView(this@TaskDetailsActivity, R.layout.activity_task_details)
        binding.setVariable(BR.viewModel, ViewModelProviders.of(this@TaskDetailsActivity).get(TaskDetailsViewModel::class.java))
        binding.executePendingBindings()

        binding.viewModel!!.setupTaskDetails(intent.extras.getSerializable(Constant.INTENT_TASK) as Task)

    }
}
