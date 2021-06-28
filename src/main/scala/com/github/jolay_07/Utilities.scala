package com.github.jolay_07

import scala.io.Source

object Utilities {

  /** //so we only do one thing save the url contents
   *
   * @param url url
   * @param filePath filePath
   * @param encoding encoding defualt UTF8
   */
  def saveUrlToFile(url: String, filePath: String, encoding: String = "utf-8"): Unit = {
    //you can use example from DownloadFiles
    //only challenge is to extract last part from the url and add it to folder
    println(s"Will open $url")
    //FIXME change iso to encoding
    val txtBuffer = Source.fromURL(url, encoding)
    val lines = txtBuffer.getLines().toArray //so we will exhaust our buffer here
    //    println(s"Dry run for lines: ${lines.length}") //notice this was 0 after we called getLines
    Utilities.saveLines(lines, filePath)
  }
  /**
   *
   * @param srcPath source Path
   * @param encoding default UTF8
   * @return Array of String Lines
   */
  def getLinesFromFile(srcPath: String, encoding:String="UTF8"): Array[String] = {
    val bufferedSource = Source.fromFile(srcPath, enc=encoding)
    val lines = bufferedSource.getLines().toArray
    bufferedSource.close
    lines
  }

  def saveLines(lines: Seq[String], destPath: String, sep: String = "\n"): Unit = {
    val txt = lines.mkString(sep)

    import java.io.{File, PrintWriter} //explicit import
    //import java.io._ //this was wildcard import meaning we got all of java.io library which we might not need
    val pw = new PrintWriter(new File(destPath))
    pw.write(txt)
    pw.close()
  }

  def saveString(text: String, destPath: String): Unit = {
    import java.io.{File, PrintWriter} //explicit import
    val pw = new PrintWriter(new File(destPath))
    pw.write(text)
    pw.close()
  }
}
