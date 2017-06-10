package net.halawata.artich

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.InjectView

class DragDropCell : LinearLayout {

    @InjectView(R.id.titleView)
    var titleView: TextView? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.inject(this, this)
    }
}
