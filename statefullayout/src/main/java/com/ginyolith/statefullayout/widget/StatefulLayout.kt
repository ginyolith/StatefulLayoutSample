package com.ginyolith.statefullayout.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter

class StatefulLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var errorView : View?   = null
    private var loadingView : View? = null
    private var childView : View?   = null
    var retryButton : View? = null

    var onRetry : (() -> Unit)? = null
        set(value) {
            field = value

            if (value != null) {
                retryButton?.setOnClickListener {
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
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.StatefulLayout, 0, 0)
        try {
            a.getResourceId(R.styleable.StatefulLayout_stateful_layout_error, -999999)
                .let {
                    if (it != -999999) {
                        errorView = View.inflate(context, it, null)
                    }
                }

            a.getResourceId(R.styleable.StatefulLayout_stateful_layout_loading,-999999)
                .let {
                    if (it != -999999) {
                        loadingView = View.inflate(context, it, null)
                    }
                }

            a.getResourceId(R.styleable.StatefulLayout_stateful_layout_retry_button_id, -99999)
                .let {
                    if (it != -99999) {
                        retryButton = errorView?.findViewById(it)
                    }
                }

        } finally {
            a.recycle()
        }

        if (errorView != null) {
            addView(errorView, MATCH_PARENT, MATCH_PARENT)
            errorView?.visibility = View.GONE
        }

        if (loadingView != null) {
            addView(loadingView, MATCH_PARENT, MATCH_PARENT)
            loadingView?.visibility = View.GONE
        }

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

    companion object {
        @BindingAdapter("app:state")
        @JvmStatic
        fun setState(layout: StatefulLayout, state : State?) {
            if (state != null) {
                layout.state = state
            }
        }
    }
}