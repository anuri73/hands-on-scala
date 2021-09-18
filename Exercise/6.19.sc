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

    def stringsMatchingPrefix(s: String): Set[String] = {
        var current = Option(root)
        for (c <- s if current.nonEmpty) current = current.get.children.get(c)

        if (current.isEmpty)
            return Set()

        val result = Set.newBuilder[String]

        def fetch(current: Node, path: List[Char]): Unit = {
            if (current.hasValue)
                result += (s + path.reverse.mkString)
            for ((c, n) <- current.children)
                fetch(n, c :: path)
        }

        fetch(current.get, Nil)

        result.result()
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

println("Trie:")
println(t)

println("Contains check:")
println("mango: " + t.contains("mango"))
println("mang: " + t.contains("mang"))
println("man: " + t.contains("man"))
println("mandarin: " + t.contains("mandarin"))
println("mandarine: " + t.contains("mandarine"))

println("Prefixes matching string check:")
println("mandarine: " + t.prefixesMatchingString("mandarine"))
println("mangosteen: " + t.prefixesMatchingString("mangosteen"))
println("mango: " + t.prefixesMatchingString("mango"))
println("manible: " + t.prefixesMatchingString("manible"))

println("Strings with prefixes:")
println("man: " + t.stringsMatchingPrefix("man"))
println("mandarine: " + t.stringsMatchingPrefix("mandarine"))
println("mangosteen: " + t.stringsMatchingPrefix("mangosteen"))
println("mango: " + t.stringsMatchingPrefix("mango"))
println("manible: " + t.stringsMatchingPrefix("manible"))
