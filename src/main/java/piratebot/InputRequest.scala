package main.java.piratebot

import com.rits.cloning.Immutable

class InputRequest(val playerId : Int,
                   val inputType : InputRequestType.Value,
                   val choices : Map[String, Int]) {
    var answer : Option[Int] = Option.empty
    
    override def equals(o: Any) = o match {
        case that: InputRequest => that.hashCode.equals(this.hashCode)
        case _ => false
    }
      
    override def hashCode : Int = {
        // Deduplicate on inputType and playerId
        // this is nuts
        return playerId * 61 + inputType.id
    }
}

@Immutable
object InputRequestType extends Enumeration {
    type InputRequestType = Value
    val SelectBooty,
        PlayPirateFromHand,
        KillPirateInAdjacentDen,
        KillPirateInAnyDen,
        RevivePirateFromDiscard,
        RecruitPirateFromDen,
        DiscardAllButOneBooty,
        SellBooty,
        SellThreeBooty,
        SellMap = Value
}
