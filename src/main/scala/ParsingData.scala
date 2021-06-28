import com.github.jolay_07.Utilities

object ParsingData extends App{

  println("Let's parse some data from XML")
  val url = "http://cdr.eionet.europa.eu/lv/eu/aqd/d/envxsbkwg/REP_LV_LEGMC_20190710_D_all_v0.xml"
  val savePath = "./src/resources/xml/weather_data_LV.xml"
  Utilities.saveUrlToFile(url, savePath)

  case class weatherData(localId: String,
                         namespace: String,
                         versionId: String,
                         name: String,
                         pos: String,
                         mobile: Boolean,
                         beginPosition: String,
                         endPosition: String,
                         natlStationCode: String,
                         municipality: String,
                         EUStationCode: String,
                         stationInfo: String,

                        )
}
