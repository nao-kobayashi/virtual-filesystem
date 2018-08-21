package jp.nao.scala.oop.commands
import jp.nao.scala.oop.files.{DirEntry, Directory}
import jp.nao.scala.oop.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}
