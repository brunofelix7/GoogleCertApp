package me.brunofelix.googlecertapp.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import me.brunofelix.googlecertapp.R
import me.brunofelix.googlecertapp.data.TaskStatusEnum

class StatusView(
    context: Context,
    attrs: AttributeSet?
) : AppCompatTextView(context, attrs) {

    fun setStatus(status: TaskStatusEnum) {
        val bg = when(status) {
            TaskStatusEnum.TODO -> context.getColor(R.color.blue_700)
            TaskStatusEnum.PROGRESS -> context.getColor(R.color.yellow_700)
            TaskStatusEnum.DONE -> context.getColor(R.color.purple_500)
        }
        setTextColor(bg)
    }
}
