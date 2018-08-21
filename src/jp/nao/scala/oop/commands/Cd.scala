package jp.nao.scala.oop.commands
import jp.nao.scala.oop.files.{DirEntry, Directory}
import jp.nao.scala.oop.filesystem.State
import sun.net.ftp.FtpDirEntry

import scala.annotation.tailrec

class Cd(dir: String) extends Command {

  def doFindEntry(root: Directory, path: String): DirEntry = {
    @tailrec
    def findEntryHelper(currentDirectory: Directory, path:List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) {
        currentDirectory
      } else if (path.tail.isEmpty)  {
        currentDirectory.findEntry(path.head)
      } else {
        val nextDir = currentDirectory.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) {
          null
        } else {
          findEntryHelper(nextDir.asDirectory, path.tail)
        }
      }
    }

    //1. tokens
    val tokens : List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    //2. navigate to the correct entry
    findEntryHelper(root, tokens)
  }


  override def apply(state: State): State = {

    //1. find root
    val root = state.root
    val wd = state.wd

    //2. find the absolute path of the directory I want to do
    val absolutePath  =
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) wd.path + dir
      else wd.path + Directory.SEPARATOR + dir


    //3. find the directory to cd to, given th path
    val destinationDirectory = doFindEntry(root, absolutePath)

    //4. change the state given the new directory
    if (destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage("dir" + ": No such directory")
    else
      State(root, destinationDirectory.asDirectory)
  }
}
