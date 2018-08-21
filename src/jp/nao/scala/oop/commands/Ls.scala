package jp.nao.scala.oop.commands
import jp.nao.scala.oop.files.DirEntry
import jp.nao.scala.oop.filesystem.State

class Ls extends Command {
  override def apply(state: State): State = {
    val contents = state.wd.content
    val output = createOutput(contents)
    state.setMessage(output)
  }

  def createOutput(contents: List[DirEntry]): String = {
    if (contents.isEmpty) ""
    else {
      val entry = contents.head
      entry.name + "[" + entry.getType + "]\n" + createOutput(contents.tail)
    }
  }
}
