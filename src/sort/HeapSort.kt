package sort

/**
 * 堆排序
 */
class HeapSort : Sort {
    override fun sort(arr: MutableList<Int>) {
        val len = arr.size
        for (i in len / 2 - 1 downTo 0) {
            maxHeapify(arr, i, len)
        }
        for (i in len - 1 downTo 1) {
            arr[0] = arr[i].also { arr[i] = arr[0] }
            maxHeapify(arr, 0, i)
        }
    }

    fun maxHeapify(arr: MutableList<Int>, pos: Int, len: Int) {
        val leftChild = pos * 2 + 1
        val rightChild = pos * 2 + 2
        var maxPos = pos
        if (leftChild < len && arr[leftChild] > arr[maxPos]) {
            maxPos = leftChild
        }
        if (rightChild < len && arr[rightChild] > arr[maxPos]) {
            maxPos = rightChild
        }
        if (maxPos != pos) {
            arr[pos] = arr[maxPos].also { arr[maxPos] = arr[pos] }
            maxHeapify(arr, maxPos, len)
        }
    }
}