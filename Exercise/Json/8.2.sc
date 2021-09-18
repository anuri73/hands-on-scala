val jsonString = os.read(os.pwd / "ammonite-releases.json")

println(jsonString)

val jsonData = ujson.read(jsonString)

println(jsonData)
