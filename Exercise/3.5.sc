def flexibleFizzBuzz(handleLine: String => Unit): Unit = {
    for (i <- Range.inclusive(1, 100)) {
        handleLine(
            (i % 3, i % 5) match {
                case (0, 0) => "FizzBuzz"
                case (0, _) => "Fizz"
                case (_, 0) => "Buzz"
                case (_, _) => i.toString
            }
        )
    }
}

flexibleFizzBuzz(s => {} /* do nothing*/)

flexibleFizzBuzz(s => println(s))

var i = 0
val output = new Array[String](100)

flexibleFizzBuzz { s =>
    output(i) = s
    i += 1
}

assert(
    output.take(15).sameElements(
        Array(
            "1",
            "2",
            "Fizz",
            "4",
            "Buzz",
            "Fizz",
            "7",
            "8",
            "Fizz",
            "Buzz",
            "11",
            "Fizz",
            "13",
            "14",
            "FizzBuzz"
        )
    )
)
