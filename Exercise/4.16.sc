val grid = Array(
    Array(3, 1, 6, 5, 7, 8, 4, 9, 2),
    Array(5, 2, 9, 1, 3, 4, 7, 6, 8),
    Array(4, 8, 7, 6, 2, 9, 5, 3, 1),

    Array(2, 6, 3, 0, 1, 0, 0, 8, 0),
    Array(9, 7, 4, 8, 6, 3, 0, 0, 5),
    Array(8, 5, 1, 0, 9, 0, 6, 0, 0),

    Array(1, 3, 0, 0, 0, 0, 2, 5, 0),
    Array(0, 0, 0, 0, 0, 0, 0, 7, 4),
    Array(0, 0, 5, 2, 0, 6, 3, 0, 0)
)

def renderSudoku(grid: Array[Array[Int]]): String = {
    val rowSeparator = "\n+-------+-------+-------+\n"
    grid
        .map(
            row => row
                .map(item => if (0 == item) " " else item.toString)
                .grouped(3)
                .map(_.mkString(" "))
                .mkString("| ", " | ", " |")
        )
        .grouped(3)
        .map(_.mkString("\n"))
        .mkString(rowSeparator, rowSeparator, rowSeparator)
}

val rendered = renderSudoku(grid)

println(rendered)
