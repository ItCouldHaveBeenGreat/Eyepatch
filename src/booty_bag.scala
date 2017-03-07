import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object BootyBag {
    private val bootyBag = ArrayBuffer[Booty.Value]()
    build
    
    def draw : Booty.Value = {
        val drawn = bootyBag(Random.nextInt(bootyBag.size))
        bootyBag -= drawn
        return drawn
    }
    
    def draw(howMany : Int) : List[Booty.Value] = {
        return List.fill(howMany)(draw)
    }
    
    def build = {
        bootyBag.clear
        for (i <- 1 to 4) { bootyBag += Booty.Chest }
        for (i <- 1 to 6) { bootyBag += Booty.Jewels }
        for (i <- 1 to 10) { bootyBag += Booty.Goods }
        for (i <- 1 to 6) { bootyBag += Booty.SpanishOfficer }
        for (i <- 1 to 6) { bootyBag += Booty.Saber }
        for (i <- 1 to 8) { bootyBag += Booty.TreasureMap }
        for (i <- 1 to 10) { bootyBag += Booty.CursedMask }
    }
}