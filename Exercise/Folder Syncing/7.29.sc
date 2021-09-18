def sync(src: os.Path, dest: os.Path): Unit = {
    for (srcSubPath <- os.walk(src)) {
        val subPath = srcSubPath.subRelativeTo(src)
        val destSubPath = dest / subPath
        (os.isDir(srcSubPath), os.isDir(destSubPath)) match {
            case (true, false) | (false, true) => os.copy.over(srcSubPath, destSubPath, createFolders = true)
            case (false, false)
                if !os.exists(destSubPath)
                    || !os.read.bytes(srcSubPath).sameElements(os.read.bytes(destSubPath)) =>
                os.copy.over(srcSubPath, destSubPath, createFolders = true)
            case _ =>
        }
    }
}

os.write(os.pwd / "post" / "ABC.txt", "Hello World")

sync(os.pwd / "post", os.pwd / "post-copy")

os.exists(os.pwd / "post-copy" / "ABC.txt")

os.read(os.pwd / "post-copy" / "ABC.txt")

os.write.append(os.pwd / "post" / "ABC.txt", "\nI am Cow")

sync(os.pwd / "post", os.pwd / "post-copy")

os.read(os.pwd / "post-copy" / "ABC.txt")
