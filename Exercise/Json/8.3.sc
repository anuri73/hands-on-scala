val small1 = ujson.Arr(
    ujson.Obj("hello" -> ujson.Str("world"), "answer" -> ujson.Num(42)),
    ujson.Bool(true)
)

println(small1)

val small2 = ujson.Arr(
    ujson.Obj("hello" -> "world", "answer" -> 42),
    true
)

println(small2)

os.write(os.pwd / "out.json", small2)
