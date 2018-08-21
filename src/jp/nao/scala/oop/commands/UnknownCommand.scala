package jp.nao.scala.oop.commands
import jp.nao.scala.oop.filesystem.State

class UnknownCommand extends Command {
  override def apply(state: State): State = state.setMessage("Unknown Command!!")

}
