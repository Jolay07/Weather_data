package com.github.jolay_07

import kantan.csv.ops.toCsvOutputOps
import kantan.csv.{RowEncoder, rfc}

import scala.xml.XML

object DataParsing extends App{
  //converting XML semi structure into our own data structures
  val relativePath = "./src/resources/xml/LV_meta.xml"

  val metaXML = XML.load(relativePath)
 val files = metaXML \ "country" \ "network" \ "station"
 //val files = metaXML \\ "station"

  case class metaDataFile(station: String,
                          station_european_code: String,
                          station_local_code: String,
                          station_name: String,
                          station_description: String,
                          station_nuts_level0: String,
                          station_nuts_level1: Int,
                          station_nuts_level2: Int,
                          station_nuts_level3: Int,
                          lau_level1_code: Long,
                          lau_level2_code: Long,
                          lau_level2_name: String,
                          sabe_country_code: String,
                          sabe_unit_code: Int,
                          sabe_unit_name: String,
                          station_start_date: String,
                          station_latitude_decimal_degrees: Double,
                          station_longitude_decimal_degrees: Double,
                          station_latitude_dms: String,
                          station_longitude_dms: String,
                          station_altitude: Int,
                          type_of_station: String,
                          station_type_of_area: String,
                          station_characteristic_of_zone: String,
                          station_subcategory_rural_background: String,
                          monitoring_obj: String,
                          meteorological_parameter: String
                          )

  //REQUIRED in order to write case class directly to JSON
  implicit val metaDataFileRW = upickle.default.macroRW[metaDataFile] //necessary to make Json file

  /**
   * Own data structure for Jason file
   * @param node elements
   * @return structure for parsing
   */
  def fromXMLtoFile(node: scala.xml.Node):metaDataFile = {
   // val measurementConfiguration = node \ "station" \ "measurement_configuration"
    metaDataFile(
      station = node.attribute("Id").getOrElse("").toString, // we can get station Id, if it exist, if not, we will get nothing
      station_european_code = (node \ "station_info" \ "station_european_code").text, //if these child elements do not exist, we will get nothing ""
      station_local_code = (node \ "station_info" \ "station_local_code").text,
      station_name = (node \ "station_info" \ "station_name").text,
      station_description = (node \ "station_info" \ "station_description").text,
      station_nuts_level0 = (node \ "station_info" \ "station_nuts_level0").text,
      station_nuts_level1 = (node \ "station_info" \ "station_nuts_level1").text.toInt,
      station_nuts_level2 = (node \ "station_info" \ "station_nuts_level2").text.toInt,
      station_nuts_level3 = (node \ "station_info" \ "station_nuts_level3").text.toInt,
      lau_level1_code = (node \ "station_info" \ "lau_level1_code").text.toLong,
      lau_level2_code = (node \ "station_info" \ "lau_level2_code").text.toLong,
      lau_level2_name = (node \ "station_info" \ "lau_level2_name").text,
      sabe_country_code = (node \ "station_info" \ "sabe_country_code").text,
      sabe_unit_code = (node \ "station_info" \ "sabe_unit_code").text.toInt,
      sabe_unit_name = (node \ "station_info" \ "sabe_unit_name").text,
      station_start_date = (node \ "station_info" \ "station_start_date").text,
      station_latitude_decimal_degrees = (node \ "station_info" \ "station_latitude_decimal_degrees").text.toDouble,
      station_longitude_decimal_degrees = (node \ "station_info" \ "station_longitude_decimal_degrees").text.toDouble,
      station_latitude_dms = (node \ "station_info" \ "station_latitude_dms").text,
      station_longitude_dms = (node \ "station_info" \ "station_longitude_dms").text,
      station_altitude = (node \ "station_info" \ "station_altitude").text.toInt,
      type_of_station = (node \ "station_info" \ "type_of_station").text,
      station_type_of_area = (node \ "station_info" \ "station_type_of_area").text,
      station_characteristic_of_zone = (node \ "station_info" \ "station_characteristic_of_zone").text,
      station_subcategory_rural_background = (node \ "station_info" \ "station_subcategory_rural_background").text,
      monitoring_obj = (node \ "station_info" \ "monitoring_obj").text,
      meteorological_parameter = (node \ "station_info" \ "meteorological_parameter").text //FIXME separate all data
    )
  }
  val fileSeq = files.map(f => fromXMLtoFile(f))
  println(s"We have gotten information on ${fileSeq.length} files")
  fileSeq.slice(0,5).foreach(println)
  // write and close single file
  val singleJson = upickle.default.write(fileSeq(1), indent = 4)
  println(singleJson)
  val firstFileJsonPath = "./src/resources/json/Zoseni_LV0016R_meta.json"
  Utilities.saveString(singleJson, firstFileJsonPath) // we allready save it for once

  // write and close all files together in one Json file
  val bigJsonFile = upickle.default.write(fileSeq, 4)
  val bigJsonFilePath = "./src/resources/json/stations_latvia_meta.json"
  //Utilities.saveString(bigJsonFile, bigJsonFilePath) // we already save it for once


 // val out = java.io.File.createTempFile("./src/resources/csv/station_latvia_meta.csv", "csv")
 // val fileEncoder: RowEncoder[metaDataFile] = RowEncoder.caseEncoder(i1 = 0, i2 = 1, i3 = 2, i4 = 3, i5 = 4, i6 = 5, i7 = 6, i8 = 7, i9 = 8, i10 = 9, i11 = 10, i12 = 11, i13 = 12, i14 = 13, i15 = 14, i16 = 15, i17 = 16, i18 = 17, i19 = 18, i20 = 19, i21 = 20, i22 = 21, i23 = 22, i24 = 23, i25 = 24, i26 = 25, i27 = 26)
 // val writer = out.asCsvWriter[metaDataFile](rfc.withHeader(station, station_european_code, station_local_code, station_name, station_description,
   // station_nuts_level0, station_nuts_level1, station_nuts_level2, station_nuts_level3, lau_level1_code, lau_level2_code, lau_level2_name,
    //sabe_country_code, sabe_unit_code, sabe_unit_name, station_start_date, station_latitude_decimal_degrees, station_longitude_decimal_degrees: Double,
   // station_latitude_dms, station_longitude_dms, station_altitude, type_of_station, station_type_of_area, station_characteristic_of_zone,
   // station_subcategory_rural_background, monitoring_obj, meteorological_parameter)

  //type MetaDataFile = (String, String, String, String, String, String, Int, Int, Int, Long, Long, String, String, Int, String,
  //  String, Double, Double, String, String, Int, String, String, String, String, String, String)

//  def convertToMetaData(tokens: Seq[String]):metaDataFile = {
//    metaDataFile(
//      tokens(0),
//      tokens(1),
//      tokens(2),
//      tokens(3),
//      tokens(4),
//      tokens(5),
//      tokens(6).toInt,
//      tokens(7).toInt,
//      tokens(8).toInt,
//      tokens(9).toLong,
//      tokens(10).toLong,
//      tokens(11),
//      tokens(12),
//      tokens(13).toInt,
//      tokens(14),
//      tokens(15),
//      tokens(16).toDouble,
//      tokens(17).toDouble,
//      tokens(18),
//      tokens(19),
//      tokens(20).toInt,
//      tokens(21),
//      tokens(22),
//      tokens(23),
//      tokens(24),
//      tokens(25),
//      tokens(26)
//    )
//
//  }

 // val Data = fileSeq.tail.map(convertToMetaData(_))

 // Data.slice(0,5).foreach(println)
}
