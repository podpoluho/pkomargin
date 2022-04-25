package loans

import scala.collection.immutable.TreeMap

class LoanMargins(margins: TreeMap[Byte, TreeMap[Long, Double]]) {

  def getMargin(currentMonth: Byte, ownShare: Byte, creditAmount: Long): Option[Double] = {
    if (currentMonth <= 12) {
      Some(1.1)
    } else {
      val margin = for {
        marginsByCreditAmount <- margins.get(ownShare) orElse
          margins.maxBefore(ownShare).map(_._2)
        margin <- marginsByCreditAmount.get(creditAmount) orElse
          marginsByCreditAmount.maxBefore(creditAmount).map(_._2)
      } yield margin
      margin
    }
  }
}
