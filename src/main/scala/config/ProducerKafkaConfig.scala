package config

import com.typesafe.config.{ Config, ConfigFactory }
import org.apache.kafka.clients.producer.ProducerConfig

import java.util.Properties

object ProducerKafkaConfig {

  private val config: Config = ConfigFactory.load()

  private val bootstrapServers: String = getBootstrapServers
  private val keySerializer:    String = config.getString("kafka.key-serializer")
  private val valueSerializer:  String = config.getString("kafka.value-serializer")

  def getProducerConfig: Properties = {
    val prop = new Properties()
    prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer)
    prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer)

    if (!isDev) {
      prop.put("security.protocol", getSecurityProtocol)
      prop.put("sasl.mechanism", getSaslMechanism)
      prop.put("sasl.jaas.config", getSaslJaasConfig)
    }
    prop
  }

  private[config] def getBootstrapServers: String = config.getString("kafka.bootstrap-servers")

  private def isDev: Boolean = config.getBoolean("kafka.isDev")

  private[config] def getSaslJaasConfig: String = {
    val username = config.getString("kafka.username")
    val password = config.getString("kafka.password")
    s"""org.apache.kafka.common.security.plain.PlainLoginModule required username="$username" password="$password";"""
  }

  private[config] def getSecurityProtocol: String = config.getString("kafka.security-protocol")

  private[config] def getSaslMechanism: String = config.getString("kafka.sasl-mechanism")
}
