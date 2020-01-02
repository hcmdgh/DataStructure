package test

import sort.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun main() {
//    buildSamples()
    test(QuickSort())
}

/**
 * 构建测试样本
 */
fun buildSamples() {
    val writer = File("sort_samples.txt").bufferedWriter()
    val size = 10000
    val round = 100
    writer.write("-1\n")
    repeat(round) {
        for (i in 0 until size) {
            val num = (0..10000).random()
            writer.write("$num ")
        }
        writer.write("-1\n")
    }
    for (i in 0 until size) {
        writer.write("$i ")
    }
    writer.write("-1\n")
    for (i in size downTo 1) {
        writer.write("$i ")
    }
    writer.write("-1\n")
    writer.close()
}

/**
 * 测试某排序算法的实现是否正确，同时计时
 */
fun test(sort: Sort) {
    val samples = ArrayList<ArrayList<Int>>()
    val inputStream = File("sort_samples.txt").inputStream()
    val scanner = Scanner(inputStream)
    while (scanner.hasNext()) {
        val sample = ArrayList<Int>()
        while (true) {
            val num = scanner.nextInt()
            if (num == -1)
                break
            sample.add(num)
        }
        samples.add(sample)
    }

    val beginTime = System.currentTimeMillis()
    var counter = 0
    for (sample in samples) {
        val _sample = ArrayList(sample)
        sort.sort(sample)
        _sample.sort()
        ++counter
        if (sample != _sample) {
            throw Error("在检测第${counter}个样本时发现错误！")
        }
    }
    val endTime = System.currentTimeMillis()
    println("检查完毕，共排序${counter}个样本，用时：${endTime - beginTime} ms")
}