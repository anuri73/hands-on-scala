def mergeSort(data: Array[Int]): Array[Int] = {
    if (data.length <= 1)
        return data

    val (left, right) = data.splitAt(data.length / 2)
    val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))
    val result = Array.newBuilder[Int]
    var (leftIdx, rightIdx) = (0, 0)
    while (leftIdx < sortedLeft.length || rightIdx < sortedRight.length) {
        val takeLeft = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match {
            case (true, false) => true
            case (false, true) => false
            case (true, true) => sortedLeft(leftIdx) < sortedRight(rightIdx)
        }
        if (takeLeft) {
            result += sortedLeft(leftIdx)
            leftIdx += 1
        } else {
            result += sortedRight(rightIdx)
            rightIdx += 1
        }
    }
    result.result()

}

println(mergeSort(Array(4, 0, 1, 5, 3, 2, 0, 0, 0)).mkString(" "))
