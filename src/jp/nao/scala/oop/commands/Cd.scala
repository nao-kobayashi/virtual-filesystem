package jp.nao.scala.oop.commands
import jp.nao.scala.oop.files.{DirEntry, Directory}
import jp.nao.scala.oop.filesystem.State

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

    @tailrec
    def collapseRelativeTokens(path: List[String], result: List[String]): List[String] = {
      /*
      /a/b => ["a", "b"]

      "b", result = list[] + "a"
       */
      if (path.isEmpty) result
      else if (".".equals(path.head)) collapseRelativeTokens(path.tail, result)
      else if ("..".equals((path.head))) {
        if (result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init)
      } else {
        collapseRelativeTokens(path.tail, result :+ path.head)
      }
    }

    //1. tokens
    val tokens : List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    //1.5 eliminate/collapse relative tokens
    //["a", "."] => ["a"]
    //["a", "b" , ".", "."] => ["a", "b"]
    //["a", ".."] => []
    // /a/b/.. => ["a", "b", ".."] => ["a"]

    val newTokens = collapseRelativeTokens(tokens, List())
    if (newTokens == null) null
    //2. navigate to the correct entry
    else findEntryHelper(root, newTokens)
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
