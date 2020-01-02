package sort

class MergeSort : Sort {
    override fun sort(arr: MutableList<Int>) {
        val len = arr.size
        val tempArr = IntArray(len)
        mergeSort(arr, 0, len - 1, tempArr)
    }

    private fun mergeSort(arr: MutableList<Int>, begin: Int, end: Int, tempArr: IntArray) {
        if (begin < end) {
            val mid = (begin + end) / 2
            mergeSort(arr, begin, mid, tempArr)
            mergeSort(arr, mid + 1, end, tempArr)
            var leftCur = begin
            var rightCur = mid + 1
            for (i in begin..end) {
                if (rightCur > end || (leftCur <= mid && arr[leftCur] < arr[rightCur])) {
                    tempArr[i] = arr[leftCur]
                    ++leftCur
                } else {
                    tempArr[i] = arr[rightCur]
                    ++rightCur
                }
            }
            for (i in begin..end) {
                arr[i] = tempArr[i]
            }
        }
    }
}