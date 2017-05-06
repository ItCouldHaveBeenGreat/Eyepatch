package libertalia

class InputRequest(val playerId : Int,
                   val inputType : InputRequestType.Value,
                   val validAnswers : Seq[String]) {
    var answer : String = ""
    var answered : Boolean = false
    
    override def equals(o: Any) = o match {
        case that: InputRequest => that.hashCode.equals(this.hashCode)
        case _ => false
    }
      
    override def hashCode : Int = {
        // Deduplicate on inputType and playerId
        return playerId * 61 + inputType.id
    }
}

object InputRequestType extends Enumeration {
    type InputRequestType = Value
    val SelectBooty,
        PlayPirateFromHand,
        KillPirateInAdjacentDen,
        KillPirateInAnyDen,
        RevivePirateFromDiscard,
        RecruitPirateFromDen,
        DiscardBooty,
        SellBooty,
        SellThreeBooty,
        SellMap = Value
}
