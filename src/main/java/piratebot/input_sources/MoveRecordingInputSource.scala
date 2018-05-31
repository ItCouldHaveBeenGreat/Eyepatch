package main.java.piratebot.input_sources

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import main.java.piratebot.{Game, InputRequest, Player}

abstract class MoveRecordingInputSource(filename: String) extends InputSource {
    def makeDecision(request: InputRequest, state: Seq[Int], game: Game): Int = {
        val decision = makeRecordedDecision(request, state, game)

        val store = new ObjectOutputStream(new FileOutputStream(filename))
        store.writeObject(state)
        store.write
        store.close()

        val in = new ObjectInputStream(new FileInputStream(filename))
        val copy = in.readObject()
        println(copy)
        println(copy.getClass)

        decision
    }

    def endGame(player: Player, players : Seq[Player])
    def endSession()
    def makeRecordedDecision(request: InputRequest, state: Seq[Int], game: Game): Int
}