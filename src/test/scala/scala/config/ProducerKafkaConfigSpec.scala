package scala.config

import config.ProducerKafkaConfig
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProducerKafkaConfigSpec extends AnyFlatSpec with Matchers {

  "ProducerKafkaConfig" should "get producer config" in {
    val producerConfig = ProducerKafkaConfig.getProducerConfig
    producerConfig.get(
      "key.serializer") shouldBe "org.apache.kafka.common.serialization.StringSerializer"
    producerConfig.get(
      "value.serializer") shouldBe "org.apache.kafka.common.serialization.StringSerializer"
  }

}
