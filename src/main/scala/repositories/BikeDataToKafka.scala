package repositories

import io.circe.Json
import io.github.chronoscala.Imports._
import org.apache.kafka.clients.producer.{ KafkaProducer, ProducerRecord }
import org.slf4j.{ Logger, LoggerFactory }

import scala.util.{ Failure, Try }

class BikeDataToKafka(producer: KafkaProducer[String, String]) {

  private val topic = "bike_data"
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def sendDataToKafka(bikeData: Json): Try[Unit] = {
    val message = bikeData.noSpaces
    val key     = createKeyWithCurrentTime
    val record  = new ProducerRecord[String, String](topic, key, message)

    Try {
      producer.send(record)
      logger.info(s"Sent to Kafka (Message size: ${message.getBytes("UTF-8").length / 1024} KB)")
    }.recoverWith { case e: Exception =>
      logger.error(s"Failed to send data to Kafka: ${e.getMessage}", e)
      Failure(e)
    }
  }

  private def createKeyWithCurrentTime: String =
    Instant.now.toEpochMilli.toString
}
