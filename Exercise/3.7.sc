import java.io.{BufferedReader, BufferedWriter}
import java.nio.file.Files.{newBufferedReader, newBufferedWriter}

def withFileWriter[T](fileName: String)(handler: BufferedWriter => T): T = {
    val output = newBufferedWriter(java.nio.file.Paths.get(fileName))
    try handler(output)
    finally output.close()
}

def withFileReader[T](fileName: String)(handler: BufferedReader => T): T = {
    val input = newBufferedReader(java.nio.file.Paths.get(fileName))
    try handler(input)
    finally input.close()
}

withFileWriter("File.txt") { writer =>
    writer.write("Hello\n"); writer.write("World!")
}
val result = withFileReader("File.txt") { reader =>
    reader.readLine() + "\n" + reader.readLine()
}
assert(result == "Hello\nWorld!")
