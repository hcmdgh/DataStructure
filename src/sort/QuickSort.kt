package sort

class QuickSort : Sort {
    override fun sort(arr: MutableList<Int>) {
        val len = arr.size
        quickSort(arr, 0, len - 1)
    }

    private fun quickSort(arr: MutableList<Int>, begin: Int, end: Int) {
        if (begin < end) {
            val rnd = (begin..end).random()
            arr[end] = arr[rnd].also { arr[rnd] = arr[end] }
            val pivot = arr[end]
            var cur = begin
            for (i in begin until end) {
                if (arr[i] <= pivot) {
                    arr[cur] = arr[i].also { arr[i] = arr[cur] }
                    ++cur
                }
            }
            arr[cur] = arr[end].also { arr[end] = arr[cur] }
            quickSort(arr, begin, cur - 1)
            quickSort(arr, cur + 1, end)
        }
    }
}