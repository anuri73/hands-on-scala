class Trie() {
    class Node(
                  var hasValue: Boolean,
                  var children: collection.mutable.Map[Char, Node] = collection.mutable.Map()
              ) {

        def prettyView(prefix: String = ""): String = {
            children.map { case (k, v) => prefix + k + ":\n" + v.prettyView(" " + prefix) }.mkString
        }

        override def toString: String = prettyView()
    }

    val root = new Node(false)

    def add(s: String): Unit = {
        var current = root
        for (c <- s) current = current.children.getOrElseUpdate(c, new Node(false))
        current.hasValue = true
    }

    def contains(s: String): Boolean = {
        var current = Option(root)
        for (c <- s if current.nonEmpty) current = current.get.children.get(c)

        current.exists(_.hasValue)
    }

    def prefixesMatchingStringIndexes(s: String): Set[Int] = {
        val result = Set.newBuilder[Int]
        var next = Option(root)
        for ((c, i) <- s.zipWithIndex if next.nonEmpty) {
            if (next.get.hasValue)
                result += i
            next = next.get.children.get(c)
        }

        if (next.exists(_.hasValue)) result += s.length

        result.result()
    }

    def prefixesMatchingString(s: String): Set[String] = {
        prefixesMatchingStringIndexes(s).map(s.substring(0, _))
    }

    override def toString: String = {
        root.toString
    }
}

val t = new Trie()
t.add("mango")
t.add("mandarin")
t.add("map")
t.add("man")
t.add("mali")
t.add("hello")

println(t)

println(t.contains("mango"))
println(t.contains("mang"))
println(t.contains("man"))
println(t.contains("mandarin"))
println(t.contains("mandarine"))

println(t.prefixesMatchingString("mangosteen"))
println(t.prefixesMatchingString("mango"))
println(t.prefixesMatchingString("manible"))
println(t.prefixesMatchingString("mandarine"))
