package scala.repositories

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import repositories.BikeDataLoader

class BikeDataLoaderSpec extends AnyFlatSpec with Matchers {

  "BikeDataLoader" should "handle successful API response" in {
    val bikeResult = BikeDataLoader.loadData

    bikeResult match {
      case Right(json) =>
        json.isArray shouldBe true
        json.asArray.get.size should be > 0
      case Left(error) =>
        fail(s"Test failed with error: $error")
    }
  }
}
