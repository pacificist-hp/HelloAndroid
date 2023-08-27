package com.android.pacificist.helloandroid.ui.select

import android.util.Log
import com.android.pacificist.helloandroid.TAG

class DragSelectionProcessor(private val handler: ISelectionHandler) :
    DragSelectTouchListener.OnDragListener {

    /**
     * Different existing selection modes
     */
    enum class Mode {
        /**
         * simply selects each item you go by and unselects on move back
         */
        Simple,

        /**
         * toggles each items original state, reverts to the original state on move back
         */
        ToggleAndUndo,

        /**
         * toggles the first item and applies the same state to each item you go by and applies inverted state on move back
         */
        FirstItemDependent,

        /**
         * toggles the item and applies the same state to each item you go by and reverts to the original state on move back
         */
        FirstItemDependentToggleAndUndo
    }

    private var mode = Mode.FirstItemDependentToggleAndUndo
    private val originalSelection = HashSet<Int>()
    private var isFirstSelected = false

    override fun onDragStart(start: Int) {
        originalSelection.addAll(handler.getSelection())
        isFirstSelected = originalSelection.contains(start)

        when (mode) {
            Mode.Simple -> {
                handler.updateSelection(start, start, isSelected = true)
            }

            Mode.ToggleAndUndo, Mode.FirstItemDependent, Mode.FirstItemDependentToggleAndUndo -> {
                handler.updateSelection(start, start, !isFirstSelected)
            }
        }
    }

    override fun onDragStop(end: Int) {
        originalSelection.clear()
    }

    override fun onDragRangeChange(start: Int, end: Int, isDraggedIn: Boolean) {
        Log.d(TAG, "[$start, $end]->$isDraggedIn")
        when (mode) {
            Mode.Simple -> {
                handler.updateSelection(start, end, isDraggedIn)
            }

            Mode.ToggleAndUndo -> {
                for (i in start..end) {
                    val origin = originalSelection.contains(i)
                    val target = if (isDraggedIn) !origin else origin
                    handler.updateSelection(i, i, target)
                }
            }

            Mode.FirstItemDependent -> {
                val target = if (isDraggedIn) !isFirstSelected else isFirstSelected
                handler.updateSelection(start, end, target)
            }

            Mode.FirstItemDependentToggleAndUndo -> {
                for (i in start..end) {
                    val origin = originalSelection.contains(i)
                    val target = if (isDraggedIn) !isFirstSelected else origin
                    handler.updateSelection(i, i, target)
                }
            }
        }
    }

    interface ISelectionHandler {
        /**
         * @return the currently selected items => can be ignored for [Mode.Simple] and [Mode.FirstItemDependent]
         */
        fun getSelection(): Set<Int>

        /**
         * update your adapter and select select/unselect the passed index range, you be get a single for all modes but [Mode.Simple] and [Mode.FirstItemDependent]
         *
         * @param start      the first item of the range who's selection state changed
         * @param end         the last item of the range who's selection state changed
         * @param isSelected      true, if the range should be selected, false otherwise
         */
        fun updateSelection(start: Int, end: Int, isSelected: Boolean)
    }
}