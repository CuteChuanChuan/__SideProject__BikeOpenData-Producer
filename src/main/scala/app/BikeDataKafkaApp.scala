package app

import config.ProducerKafkaConfig
import org.apache.kafka.clients.producer.KafkaProducer

import java.util.concurrent.{ Executors, ScheduledExecutorService, TimeUnit }
import org.slf4j.{ Logger, LoggerFactory }
import repositories.{ BikeDataLoader, BikeDataToKafka }

object BikeDataKafkaApp {

  private val logger:    Logger                   = LoggerFactory.getLogger(getClass)
  private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

  def main(args: Array[String]): Unit = {
    val producer           = createProducer
    val bikeDataToKafka    = new BikeDataToKafka(producer)
    val getDataSendToKafka = setupTask(bikeDataToKafka)
    scheduler.scheduleAtFixedRate(getDataSendToKafka, 0, 1, TimeUnit.MINUTES)

    sys.addShutdownHook {
      scheduler.shutdown()
      producer.close()
      logger.info("Kafka producer and scheduler shut down")
    }
  }

  private def createProducer: KafkaProducer[String, String] = {
    val producer = new KafkaProducer[String, String](ProducerKafkaConfig.getProducerConfig)
    logger.info("Created a Kafka producer")
    producer
  }

  private def setupTask(bikeDataToKafka: BikeDataToKafka): Runnable = () =>
    BikeDataLoader.loadData match {
      case Right(value) => bikeDataToKafka.sendDataToKafka(value)
      case Left(error)  => println(s"Failed to load data: $error")
    }
}
