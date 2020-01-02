package sort

class InsertionSort : Sort {
    override fun sort(arr: MutableList<Int>) {
        val len = arr.size
        for (i in 1 until len) {
            val num = arr[i]
            var pos = i - 1
            while (pos >= 0 && arr[pos] > num) {
                arr[pos + 1] = arr[pos]
                --pos
            }
            arr[pos + 1] = num
        }
    }
}