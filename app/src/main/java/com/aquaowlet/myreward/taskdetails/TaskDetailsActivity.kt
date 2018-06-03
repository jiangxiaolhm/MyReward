package com.aquaowlet.myreward.taskdetails

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.databinding.library.baseAdapters.BR
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.addedittask.AddEditTaskActivity
import com.aquaowlet.myreward.data.Task
import com.aquaowlet.myreward.databinding.ActivityTaskDetailsBinding
import com.aquaowlet.myreward.util.Constant
import kotlinx.android.synthetic.main.activity_task_details.*

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

        // Setup task attributes to be displayed on the screen.
        binding.viewModel!!.setupTaskDetails(intent.extras.getSerializable(Constant.INTENT_TASK) as Task)

        return_action.setOnRippleCompleteListener {
            finish()
        }
        edit_action.setOnRippleCompleteListener {
            val intent = Intent(this@TaskDetailsActivity, AddEditTaskActivity::class.java)
            intent.putExtra(Constant.INTENT_TYPE, Constant.INTENT_EDIT_TASK)
            intent.putExtra(Constant.INTENT_TASK, binding.viewModel!!.task!!)
            startActivityForResult(intent, Constant.INTENT_EDIT_TASK_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constant.INTENT_EDIT_TASK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val updatedTask = data?.extras?.getSerializable(Constant.INTENT_RESULT) as Task?
                if (updatedTask != null) {
                    binding.viewModel!!.setupTaskDetails(updatedTask)
                }
            }
        }
    }
}
