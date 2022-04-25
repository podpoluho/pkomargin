package loans

import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.junit.JUnitRunner

import scala.collection.immutable.TreeMap

@RunWith(classOf[JUnitRunner])
class LoanMarginsSpec extends AnyFunSpec {

  describe("LoanMargin") {
    // table doesn't contain all data from pdf example
    val pkoMargins = new LoanMargins(
      TreeMap(
        10.toByte -> TreeMap(
          0L -> 3.94,
          40000L -> 2.49,
          80000L -> 2.17,
          120000L -> 2.05,
          200000L -> 1.83,
          700000L -> 1.74
        ),
        50.toByte -> TreeMap(
          0L -> 2.94,
          700000L -> 1.45
        )
      )
    )

    it("should return margin 1.1 in first 12 month") {
      assertResult(Some(1.1)) {
        pkoMargins.getMargin(1, 10, 1)
      }

      assertResult(Some(1.1)) {
        pkoMargins.getMargin(12, 10, 1)
      }
    }

    it("should return no margin if own share < 10") {
      assertResult(None) {
        pkoMargins.getMargin(13, 0, 800000)
      }

      assertResult(None) {
        pkoMargins.getMargin(13, 9, 800000)
      }
    }

    it("should return margins based on the pko margin table") {
      assertResult(Some(3.94)) {
        pkoMargins.getMargin(13, 10, 1)
      }

      assertResult(Some(2.49)) {
        pkoMargins.getMargin(13, 10, 40000)
      }

      assertResult(Some(2.49)) {
        pkoMargins.getMargin(13, 11, 40001)
      }

      assertResult(Some(2.17)) {
        pkoMargins.getMargin(13, 11, 80000)
      }

      assertResult(Some(1.74)) {
        pkoMargins.getMargin(13, 11, 800000)
      }

      assertResult(Some(2.94)) {
        pkoMargins.getMargin(13, 50, 10000)
      }

      assertResult(Some(1.45)) {
        pkoMargins.getMargin(13, 100, 700000)
      }
    }
  }
}
