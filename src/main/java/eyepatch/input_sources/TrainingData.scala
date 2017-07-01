package main.java.eyepatch.input_sources

class TrainingData(val playerId : Int, val decisionType : Int, val gameState : Seq[Int], val decision : Int, val agent : String) {
    var annotation = Map[String, String]()
    
    def setAnnotation(attributes : Map[String,String]) = {
        annotation = attributes
    }
    
    def buildJson : String = {
        val input = "[" + playerId.toString + ", " + decisionType.toString + ", " + gameState.mkString(", ") + "]"
        val annotationText = "{" + annotation.map( kv => "\"" + kv._1.toString + "\": \"" + kv._2.toString + "\"" ).mkString(", ") + "}"
        return "{\"input\": " + input + ", \"output\": " + decision.toString + ", \"agent\": \"" + agent + "\", \"annotation\": " + annotationText + "}"
    }
}

