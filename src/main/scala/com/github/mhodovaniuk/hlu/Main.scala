package com.github.mhodovaniuk.hlu

import java.io.{BufferedReader, File, FileReader}
import java.util.regex.Pattern

import resource._
import scopt.OptionParser

import scala.util.matching.Regex

object Main {
  private val CommandLineArgsParser = new OptionParser[Config]("Hibernate Log Util") {
    opt[File]("lf").required().valueName("<log file>")
      .action((logFile, config) => config.copy(logFile = logFile))
  }

  private val BindingRegex = raw".*\[(.*)\].*\[(.*)\].*\[(.*)\].*".r

  def main(args: Array[String]): Unit = {
    CommandLineArgsParser.parse(args, Config()) match {
      case Some(config) =>
        for (reader <- managed(new BufferedReader(new FileReader(config.logFile)))) {
          var mbSql: Option[String] = None
          Stream.continually(reader.readLine())
            .takeWhile(_ != null)
            .foreach(line => {
              if (line.contains("SqlStatementLogger")) {
                if (mbSql.isDefined) {
                  println(mbSql.get)
                }
                mbSql = Some(line)
              } else if (line.contains("BasicBinder") && mbSql.isDefined) {
                mbSql = mbSql.map(sql => {
                  line match {
                    case BindingRegex(_, _, parameterValue) => sql.replace("?", parameterValue)
                    case _ => sql
                  }
                })
              }
            })
        }
    }
  }
}

