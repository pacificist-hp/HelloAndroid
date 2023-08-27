package com.android.pacificist.helloandroid.ui.select

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.OverScroller
import androidx.recyclerview.widget.RecyclerView
import com.android.pacificist.helloandroid.TAG

import kotlin.math.max
import kotlin.math.min

// https://github.com/MFlisar/DragSelectRecyclerView
class DragSelectTouchListener(
    private val recyclerView: RecyclerView,
    private val dragListener: OnDragListener
) : RecyclerView.OnItemTouchListener {

    /**
     * 是否开启滑动多选
     */
    private var isActive = false

    /**
     * 手指按下的位置
     */
    private var start = RecyclerView.NO_POSITION

    /**
     * 手指当前的位置
     */
    private var end = RecyclerView.NO_POSITION

    private var lastStart = RecyclerView.NO_POSITION
    private var lastEnd = RecyclerView.NO_POSITION

    /**
     * 手指是否在顶部滚动区内
     */
    private var inTopSpot = false

    /**
     * 手指是否在底部滚动区内
     */
    private var inBottomSpot = false

    /**
     * 上下自动滚动区高度px
     */
    private val spotHeight = 56

    /**
     * 顶部自动滚动区距离列表顶部偏移
     */
    private val topSpotOffset = 0

    /**
     * 底部自动滚动区距离列表底部偏移
     */
    private val bottomSpotOffset = 0

    /**
     * 手指在上部自动滚动区上方时是否自动滚动
     */
    private val isScrollAboveTopSpot = true

    /**
     * 手指在底部自动滚动区下方时是否滚动
     */
    private val isScrollBelowBottomSpot = true

    /**
     * 顶部自动滚动区起点
     */
    private var topSpotFrom = 0

    /**
     * 顶部自动滚动区终点
     */
    private var topSpotTo = 0

    /**
     * 底部自动滚动区起点
     */
    private var bottomSpotFrom = 0

    /**
     * 底部自动滚动区终点
     */
    private var bottomSpotTo = 0

    /**
     * 列表一次滑动最大距离px
     */
    private val maxScrollDistance = 16

    private var scrollDistance = 0
    private var scrollSpeedFactor = 0f

    private var lastX = Float.MIN_VALUE
    private var lastY = Float.MIN_VALUE

    /**
     * 自动滚动组件
     */
    private val scroller = OverScroller(recyclerView.context, LinearInterpolator())

    private val scrollRunnable = object : Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                scrollBy(scrollDistance)
                recyclerView.postOnAnimation(this)
            }
        }
    }

    /**
     * 开启滑动多选
     *
     * @param position 起始元素位置
     */
    fun startDragSelection(position: Int) {
        Log.d(TAG, "position=$position")
        isActive = true
        start = position
        end = position
        lastStart = position
        lastEnd = position
        dragListener.onDragStart(position)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if (!isActive || (rv.adapter?.itemCount ?: 0) == 0) {
            return false
        }

        topSpotFrom = 0 + topSpotOffset
        topSpotTo = 0 + topSpotOffset + spotHeight
        bottomSpotFrom = rv.height + bottomSpotOffset - spotHeight
        bottomSpotTo = rv.height + bottomSpotOffset
        return true
    }

    private fun startAutoScroll() {
        if (scroller.isFinished) {
            recyclerView.removeCallbacks(scrollRunnable)
            scroller.startScroll(0, scroller.currY, 0, 5000, 100000)
            recyclerView.postOnAnimation(scrollRunnable)
        }
    }

    private fun stopAutoScroll() {
        if (!scroller.isFinished) {
            recyclerView.removeCallbacks(scrollRunnable)
            scroller.abortAnimation()
        }
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        if (!isActive) {
            return
        }

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                if (!inTopSpot && !inBottomSpot) {
                    updateDragRange(rv, e.x, e.y)
                }
                processAutoScroll(e)
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                reset()
            }
        }
    }

    /**
     * 更新手指滑动覆盖的范围
     *
     * @param rv 列表
     * @param x 手指x位置
     * @param y 手指y位置
     */
    private fun updateDragRange(rv: RecyclerView, x: Float, y: Float) {
        val position = findChildViewPositionByPoint(rv, x, y)
        Log.d(TAG, "position=$position, end=$end, point at [$x, $y]")
        if (position != RecyclerView.NO_POSITION && end != position) {
            end = position
            notifyDragRangeChanged()
        }
    }

    /**
     * 根据手指位置，找到被滑动覆盖的列表中的元素位置
     *
     * @param rv 列表
     * @param x 手指x位置
     * @param y 手指y位置
     * @return 元素位置
     */
    private fun findChildViewPositionByPoint(rv: RecyclerView, x: Float, y: Float): Int {
        val count = rv.layoutManager?.childCount ?: 0
        var leftTopPosition = RecyclerView.NO_POSITION
        var rightBottomPosition = RecyclerView.NO_POSITION
        for (i in count - 1 downTo 0) {
            rv.layoutManager?.getChildAt(i)?.let {
                if (isViewContainsPoint(it, x, y)) {
                    return rv.getChildAdapterPosition(it)
                } else if (isViewRightBottomToPoint(it, x, y)) {
                    rightBottomPosition = rv.getChildAdapterPosition(it)
                } else if (isViewLeftTopToPoint(it, x, y)) {
                    leftTopPosition = rv.getChildAdapterPosition(it)
                } else {
                    Log.w(
                        TAG, "should not get here, " +
                                "view at [${it.left}, ${it.top}, ${it.right}, ${it.bottom}], " +
                                "point at [$x, $y]"
                    )
                }
            }

            if (leftTopPosition != RecyclerView.NO_POSITION) {
                break
            }
        }

        return if (leftTopPosition >= start) {
            leftTopPosition
        } else if (rightBottomPosition in 0..start) {
            rightBottomPosition
        } else {
            RecyclerView.NO_POSITION
        }
    }

    private fun isViewLeftTopToPoint(v: View, x: Float, y: Float): Boolean =
        y > v.bottom + v.translationY || (y in v.top + v.translationY..v.bottom + v.translationY && x > v.right + v.translationX)

    private fun isViewRightBottomToPoint(v: View, x: Float, y: Float): Boolean =
        y < v.top + v.translationY || (y in v.top + v.translationY..v.bottom + v.translationY && x < v.left + v.translationX)

    private fun isViewContainsPoint(v: View, x: Float, y: Float): Boolean =
        x in v.left + v.translationX..v.right + v.translationX && y in v.top + v.translationY..v.bottom + v.translationY

    /**
     * 根据手指位置开始或停止自动滚动
     *
     * @param event 手指触摸事件
     */
    private fun processAutoScroll(event: MotionEvent) {
        val y = event.y.toInt()
        if (y in topSpotFrom..topSpotTo) {
            // 手指在顶部滚动区内
            lastX = event.x
            lastY = event.y
            scrollSpeedFactor =
                (topSpotTo.toFloat() - topSpotFrom.toFloat() - (y.toFloat() - topSpotFrom.toFloat())) / (topSpotTo.toFloat() - topSpotFrom.toFloat())
            scrollDistance = (maxScrollDistance.toFloat() * scrollSpeedFactor * -1f).toInt()
            if (!inTopSpot) {
                inTopSpot = true
                startAutoScroll()
            }
        } else if (isScrollAboveTopSpot && y < topSpotFrom) {
            // 手指在顶部滚动区上方
            lastX = event.x
            lastY = event.y
            scrollDistance = maxScrollDistance * -1
            if (!inTopSpot) {
                inTopSpot = true
                startAutoScroll()
            }
        } else if (y in bottomSpotFrom..bottomSpotTo) {
            // 手指在底部滚动区内
            lastX = event.x
            lastY = event.y
            scrollSpeedFactor =
                (y.toFloat() - bottomSpotFrom.toFloat()) / (bottomSpotTo.toFloat() - bottomSpotFrom.toFloat())
            scrollDistance = (maxScrollDistance.toFloat() * scrollSpeedFactor).toInt()
            if (!inBottomSpot) {
                inBottomSpot = true
                startAutoScroll()
            }
        } else if (isScrollBelowBottomSpot && y > bottomSpotTo) {
            // 手指在底部滚动区下方
            lastX = event.x
            lastY = event.y
            scrollDistance = maxScrollDistance
            if (!inTopSpot) {
                inTopSpot = true
                startAutoScroll()
            }
        } else {
            // 手指在列表中间
            inBottomSpot = false
            inTopSpot = false
            lastX = Float.MIN_VALUE
            lastY = Float.MIN_VALUE
            stopAutoScroll()
        }
    }

    /**
     * 计算滑动覆盖的变化范围，回调监听
     */
    private fun notifyDragRangeChanged() {
        if (start == RecyclerView.NO_POSITION || end == RecyclerView.NO_POSITION
            || lastStart == RecyclerView.NO_POSITION || lastEnd == RecyclerView.NO_POSITION
        ) {
            Log.w(TAG, "should not get here: $start, $end, $lastStart, $lastEnd")
            return
        }

        val newStart = min(start, end)
        val newEnd = max(start, end)

        if (newStart > lastStart) {
            // 取消滑动向前覆盖
            dragListener.onDragRangeChange(lastStart, newStart - 1, false)
        } else if (newStart < lastStart) {
            // 滑动向前覆盖
            dragListener.onDragRangeChange(newStart, lastStart - 1, true)
        }

        if (newEnd > lastEnd) {
            // 滑动向后覆盖
            dragListener.onDragRangeChange(lastEnd + 1, newEnd, true)
        } else if (newEnd < lastEnd) {
            // 取消滑动向后覆盖
            dragListener.onDragRangeChange(newEnd + 1, lastEnd, false)
        }

        lastStart = newStart
        lastEnd = newEnd
    }

    private fun reset() {
        isActive = false
        dragListener.onDragStop(end)

        start = RecyclerView.NO_POSITION
        end = RecyclerView.NO_POSITION
        lastStart = RecyclerView.NO_POSITION
        lastEnd = RecyclerView.NO_POSITION
        inTopSpot = false
        inBottomSpot = false
        lastX = Float.MIN_VALUE
        lastY = Float.MIN_VALUE
        stopAutoScroll()
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    private fun scrollBy(distance: Int) {
        val scrollDistance =
            if (distance > 0) distance.coerceAtMost(maxScrollDistance)
            else distance.coerceAtLeast(-maxScrollDistance)
        recyclerView.scrollBy(0, scrollDistance)
        if (lastX != Float.MIN_VALUE && lastY != Float.MIN_VALUE) {
            updateDragRange(recyclerView, lastX, lastY)
        }
    }

    interface OnDragListener {
        /**
         * 滑动覆盖范围变化回调
         *
         * @param start       the newly dragged in(out) range start
         * @param end         the newly dragged in(out) range end
         * @param isDraggedIn true, range got dragged in, false if not
         */
        fun onDragRangeChange(start: Int, end: Int, isDraggedIn: Boolean)

        /**
         * 滑动开始回调
         *
         * @param start the item on which the drag selection was started at
         */
        fun onDragStart(start: Int)

        /**
         * 滑动结束回调
         *
         * @param end the item on which the drag selection was stopped at
         */
        fun onDragStop(end: Int)
    }
}