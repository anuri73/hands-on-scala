def mergeSort[T: Ordering](data: IndexedSeq[T]): IndexedSeq[T] = {
    if (data.length <= 1)
        return data

    val (left, right) = data.splitAt(data.length / 2)
    val (sortedLeft, sortedRight) = (mergeSort(left), mergeSort(right))
    val result = IndexedSeq.newBuilder[T]
    var (leftIdx, rightIdx) = (0, 0)
    while (leftIdx < sortedLeft.length || rightIdx < sortedRight.length) {
        val takeLeft: Boolean = (leftIdx < sortedLeft.length, rightIdx < sortedRight.length) match {
            case (true, false) => true
            case (false, true) => false
            case (true, true) => Ordering[T].lt(sortedLeft(leftIdx), sortedRight(rightIdx))
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

println(mergeSort(Vector(5, 3, 4, 2, 1)).mkString(" "))

println(mergeSort(Vector("banana", "apple", "durian", "cabbage")).mkString(" "))
