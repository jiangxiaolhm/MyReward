/*
 * Created by Eric Hongming Lin on 28/05/18 3:01 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 28/05/18 2:54 AM
 */

package com.aquaowlet.myreward.addedittask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.util.Constant
import kotlinx.android.synthetic.main.activity_add_edit_task.*
import java.util.*
import android.databinding.DataBindingUtil
import android.widget.EditText
import com.android.databinding.library.baseAdapters.BR
import com.aquaowlet.myreward.databinding.ActivityAddEditTaskBinding
import com.aquaowlet.myreward.util.Util

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@AddEditTaskActivity, R.layout.activity_add_edit_task)
        binding.setVariable(BR.viewModel, ViewModelProviders.of(this).get(AddEditTaskViewModel::class.java))
        binding.executePendingBindings()

        setTaskNameListener()
        setStartAndDueDateListener()
        setRepeatTaskListener()
        setButtonsListener()

        val intentType = intent.extras.getString(Constant.INTENT_TYPE)
        when (intentType) {
            Constant.INTENT_ADD_TASK -> {
                // TODO
                setTitle(R.string.title_new_task)
            }
            Constant.INTENT_EDIT_TASK -> {
                // TODO
                setTitle(R.string.title_edit_task)
            }
            else -> {
                Log.e(Constant.ERROR, "Unknown intent.")
            }
        }
    }

    /**
     * Set listener to validate the task name EditText.
     */
    private fun setTaskNameListener() {

        text_add_edit_task_name.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.viewModel!!.name.set((v as EditText).text.toString())
                validateTaskName()
            }
        }
    }

    /**
     * Set listeners to pick date and time.
     */
    private fun setStartAndDueDateListener() {

        text_add_edit_task_start_at.setOnClickListener {
            showDatePicker(it as EditText)
        }
        text_add_edit_task_due_at.setOnClickListener {
            showDatePicker(it as EditText)
        }
    }

    /**
     * Set listeners change the visibility of repeat task details.
     */
    private fun setRepeatTaskListener() {

        checkbox_repeatable.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                row_add_edit_task_period.visibility = View.VISIBLE
            } else {
                row_add_edit_task_period.visibility = View.GONE
            }
        }
        spinner_add_edit_task_period.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent?.getItemAtPosition(position).toString() == resources.getString(R.string.add_edit_task_period_spinner_custom_days)) {
                    text_add_edit_task_custom_period.visibility = View.VISIBLE
                } else {
                    clearAlertTint(text_add_edit_task_custom_period)
                    text_add_edit_task_custom_period.visibility = View.GONE
                }
            }
        }
        text_add_edit_task_custom_period.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.viewModel!!.customPeriod.set((v as EditText).text.toString())
                validateCustomPeriod()
            }
        }
    }

    /**
     * Set listeners to buttons to perform actions.
     */
    private fun setButtonsListener() {

        confirm_action.setOnRippleCompleteListener {

            updateBindingVariables()

            if (validateForm()) {
                // TODO add new task
                binding.viewModel!!.addTask()
            }
        }
        cancel_action.setOnRippleCompleteListener {
            finish()
        }
    }

    /**
     * Validate if the task name is empty.
     */
    private fun validateTaskName(): Boolean {

        if (binding.viewModel!!.validateTaskName()) {
            clearAlertTint(text_add_edit_task_name)
        } else {
            setAlertTint(text_add_edit_task_name)
            return false
        }
        return true
    }

    /**
     * Validate the start date and due date..
     */
    private fun validateStartAndDueDate(): Boolean {

        var isValid = true

        if (binding.viewModel!!.validateDate(text_add_edit_task_start_at.text.toString())) {
            clearAlertTint(text_add_edit_task_start_at)
            text_add_edit_task_start_at.hint = getString(R.string.add_edit_task_optional_hint)
        } else {
            setAlertTint(text_add_edit_task_start_at)
            binding.viewModel!!.startAt.set("")
            text_add_edit_task_start_at.hint = getString(R.string.add_edit_task_date_validation_message)
            isValid = false
        }

        if (binding.viewModel!!.validateDate(text_add_edit_task_due_at.text.toString())) {
            clearAlertTint(text_add_edit_task_due_at)
            text_add_edit_task_due_at.hint = getString(R.string.add_edit_task_optional_hint)
        } else {
            setAlertTint(text_add_edit_task_due_at)
            binding.viewModel!!.dueAt.set("")
            text_add_edit_task_due_at.hint = getString(R.string.add_edit_task_date_validation_message)
            isValid = false
        }

        if (text_add_edit_task_start_at.text.toString().isNotEmpty() && text_add_edit_task_due_at.text.toString().isNotEmpty()) {
            if (binding.viewModel!!.validateStartAndDueDate(text_add_edit_task_start_at.text.toString(), text_add_edit_task_due_at.text.toString())) {
                clearAlertTint(text_add_edit_task_start_at)
                clearAlertTint(text_add_edit_task_due_at)

                text_add_edit_task_start_at.hint = getString(R.string.add_edit_task_optional_hint)
                text_add_edit_task_due_at.hint = getString(R.string.add_edit_task_optional_hint)
            } else {
                setAlertTint(text_add_edit_task_start_at)
                setAlertTint(text_add_edit_task_due_at)

                binding.viewModel!!.startAt.set("")
                binding.viewModel!!.dueAt.set("")

                text_add_edit_task_start_at.hint = getString(R.string.add_edit_task_start_and_due_date_validation_message)
                text_add_edit_task_due_at.hint = getString(R.string.add_edit_task_start_and_due_date_validation_message)
                isValid = false
            }
        }

        return isValid
    }

    /**
     * Validate if the custom period is empty.
     */
    private fun validateCustomPeriod(): Boolean {

        if (spinner_add_edit_task_period.selectedItem.toString() == resources.getString(R.string.add_edit_task_period_spinner_custom_days)) {
            if (binding.viewModel!!.validateCustomPeriod()) {
                clearAlertTint(text_add_edit_task_custom_period)
            } else {
                setAlertTint(text_add_edit_task_custom_period)
                return false
            }
        }
        return true
    }

    /**
     * Validate the user input data, return if all input fields are valid.
     */
    private fun validateForm(): Boolean {
        var isValid = true

        isValid = isValid && validateTaskName()
        isValid = isValid && validateStartAndDueDate()
        isValid = isValid && validateCustomPeriod()

        if (isValid) {
            println("Yes")
        } else {
            println("No")
        }
        return isValid
    }

    private fun setAlertTint(editText: EditText) {
        editText.background.mutate().setColorFilter(resources.getColor(R.color.color_alert), PorterDuff.Mode.SRC_ATOP)
        editText.setHintTextColor(resources.getColor(R.color.color_alert))
    }

    private fun clearAlertTint(editText: EditText) {
        editText.background.mutate().clearColorFilter()
        editText.setHintTextColor(resources.getColor(R.color.color_hint_default))
    }


    /**
     * Show dialog to pick date and time.
     */
    private fun showDatePicker(editText: EditText) {

        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                editText.setText(Util.simpleDateFormat.format(cal.time))
            }
            TimePickerDialog(
                    this@AddEditTaskActivity,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false).show()
        }
        DatePickerDialog(
                this@AddEditTaskActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateBindingVariables() {
        binding.viewModel!!.name.set(text_add_edit_task_name.text.toString())
        binding.viewModel!!.reward.set(text_add_edit_task_reward.text.toString())
        binding.viewModel!!.punishment.set(text_add_edit_task_punishment.text.toString())
        binding.viewModel!!.startAt.set(text_add_edit_task_start_at.text.toString())
        binding.viewModel!!.dueAt.set(text_add_edit_task_due_at.text.toString())
        binding.viewModel!!.customPeriod.set(text_add_edit_task_custom_period.text.toString())
        binding.viewModel!!.description.set(text_add_edit_task_description.text.toString())
    }
}
