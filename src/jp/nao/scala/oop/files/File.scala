package jp.nao.scala.oop.files

import jp.nao.scala.oop.filesystem.FilesystemException


class File (override val parentPath: String, override val name: String, val contents: String)
extends  DirEntry (parentPath, name) {

  override def asDirectory: Directory =
    throw new FilesystemException("A File cannot be converted to a directory!")

  override def asFile: File = this

  override def getType: String = "File"

  def setContents(newContents: String): File =
    new File(parentPath, name, newContents)

  def appendContents(newContents: String): File =
    setContents(contents + "\n" + newContents)


  def isDirectory: Boolean = false
  def isFile: Boolean = true
}


object File {
  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}