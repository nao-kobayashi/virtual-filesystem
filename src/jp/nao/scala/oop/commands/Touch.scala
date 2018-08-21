package jp.nao.scala.oop.commands
import jp.nao.scala.oop.files.{DirEntry, File}
import jp.nao.scala.oop.filesystem.State

class Touch(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)

}
