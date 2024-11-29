package scala.repositories

import io.circe.Json
import org.apache.kafka.clients.producer.{ KafkaProducer, ProducerRecord }
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{ verify, when }
import org.mockito.MockitoSugar.mock
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{ Failure, Success, Try }

class BikeDataToKafkaSpec extends AnyFlatSpec with Matchers {
  "sendDataToKafka" should "send data to Kafka" in {
    val mockProducer = mock[KafkaProducer[String, String]]
    val bikeData     = Json.obj("stationId" -> Json.fromString("123"))

    when(mockProducer.send(any[ProducerRecord[String, String]])).thenReturn(null)
    val bikeDataToKafka = new {
      val topic = "test_topic"
      def sendDataToKafka(bikeData: Json): Try[Unit] =
        Try {
          val messages = bikeData.noSpaces
          val record   = new ProducerRecord[String, String](topic, messages)
          mockProducer.send(record)
        }
    }

    val result = bikeDataToKafka.sendDataToKafka(bikeData)

    result shouldBe Success(())
    verify(mockProducer).send(any[ProducerRecord[String, String]])
  }
  it should "handle failure when sending data" in {
    val mockProducer = mock[KafkaProducer[String, String]]
    val bikeData     = Json.obj("stationId" -> Json.fromString("123"))

    when(mockProducer.send(any[ProducerRecord[String, String]]))
      .thenThrow(new RuntimeException("Kafka send failed"))

    val bikeDataToKafka = new {
      val topic = "test_topic"
      def sendDataToKafka(bikeData: Json): Try[Unit] =
        Try {
          val message = bikeData.noSpaces
          val record  = new ProducerRecord[String, String](topic, message)
          mockProducer.send(record)
        }
    }

    val result = bikeDataToKafka.sendDataToKafka(bikeData)

    result shouldBe a[Failure[_]]
    result.failed.get.getMessage should include("Kafka send failed")
    verify(mockProducer).send(any[ProducerRecord[String, String]])
  }
}
