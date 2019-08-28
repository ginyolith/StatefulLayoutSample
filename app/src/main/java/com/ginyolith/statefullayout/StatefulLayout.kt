package com.ginyolith.statefullayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.FrameLayout

class StatefulLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val errorView = View.inflate(context, R.layout.view_error_common, null)
    private val loadingView = View.inflate(context, R.layout.view_loading_common, null)
    private var childView : View? = null
    var onRetry : (() -> Unit)? = null
        set(value) {
            field = value

            if (value != null) {
                errorView.findViewById<Button>(R.id.retryButton)?.setOnClickListener {
                    value()
                }
            }
        }

    var state : State = State.Display
        set(value) {
            field = value

            when(state) {
                State.Loading -> {
                    childView?.visibility = View.GONE
                    errorView?.visibility = View.GONE
                    loadingView?.visibility = View.VISIBLE
                }
                State.Display -> {
                    childView?.visibility = View.VISIBLE
                    errorView?.visibility = View.GONE
                    loadingView?.visibility = View.GONE
                }
                State.Error -> {
                    childView?.visibility = View.GONE
                    errorView?.visibility = View.VISIBLE
                    loadingView?.visibility = View.GONE
                }
            }
        }

    init {
        addView(errorView, MATCH_PARENT, MATCH_PARENT)
        addView(loadingView, MATCH_PARENT, MATCH_PARENT)
        errorView.visibility = View.GONE
        loadingView.visibility = View.GONE
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        checkAlreadyHostsChild(child)
        super.addView(child, index, params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        checkAlreadyHostsChild(child)
        super.addView(child, width, height)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        checkAlreadyHostsChild(child)
        super.addView(child, params)
    }

    override fun addView(child: View?) {
        checkAlreadyHostsChild(child)
        super.addView(child)
    }

    override fun addView(child: View?, index: Int) {
        checkAlreadyHostsChild(child)
        super.addView(child, index)
    }

    private fun checkAlreadyHostsChild(child: View?){
        if (childCount > 2) {
            throw IllegalStateException("StatefulLayout can host only one direct child")
        }

        childView = child
    }

    enum class State { Loading, Display, Error}
}