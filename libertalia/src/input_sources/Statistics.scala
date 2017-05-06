package input_sources

import scala.collection.mutable

trait Statistics {
  private val counters = mutable.HashMap[String, Int]()

  def addCounter(counterName: String, value: Int) = {
    if (counters.contains(counterName)) {
      counters.update(counterName, counters(counterName) + value)
    } else {
      counters.put(counterName, value)
    }
  }
}
