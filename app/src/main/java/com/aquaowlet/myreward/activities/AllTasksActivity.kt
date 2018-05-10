package com.aquaowlet.myreward.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.holders.TaskTreeItemHolder
import com.github.johnkil.print.PrintConfig
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import kotlinx.android.synthetic.main.activity_all_tasks.*

class AllTasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tasks)
    }
}
