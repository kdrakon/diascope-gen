package io.policarp.diascope

import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO

import scala.collection.JavaConversions._
import java.nio.file.{Paths, Files}


object DiascopeGen extends App {

  val dir = if (args.length == 0) Paths.get(".") else Paths.get(args(0))
  val quadrantCounter = new AtomicInteger()

  val entries = Files.newDirectoryStream(dir, "*.{JPG,jpg}").toList.par.map { imagePath =>

    val fileName = imagePath.getFileName
    val image = ImageIO.read(imagePath.toFile)

    val quad = quadrantCounter.getAndIncrement % 2

    val xpoint = image.getMinX + (image.getWidth / 4) + ((image.getWidth/2) * quad)
    val ypoint = image.getMinY + (image.getHeight / 4) + ((image.getHeight/2) * quad)

    val zoomOutWidth = image.getWidth
    val zoomInWidth = image.getWidth / 4

    s"kbrn 0,30,0 $fileName xyw=$xpoint,$ypoint,$zoomInWidth xyw=0,0,$zoomOutWidth accel=1"

  }

  println(entries.mkString("\n"))

}

