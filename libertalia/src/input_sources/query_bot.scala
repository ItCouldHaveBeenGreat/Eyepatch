import scala.util.Random

class QueryBot(val url : String) extends InputSource {
    
    val START_GAME_SESSION = "start_game_session"
    
    
    def makeDecision(request: InputRequest, state: GameState) : String = {
        val response = get(url + "/" + START_GAME_SESSION + "/")
        println("URL: " + url + "/" + START_GAME_SESSION)
        println("QueryBot: " + response)
        return response
    }
    
    
    
    /**
      * http://alvinalexander.com/scala/how-to-write-scala-http-get-request-client-source-fromurl
      *
      * The `connectTimeout` and `readTimeout` comes from the Java URLConnection
      * class Javadoc.
      * @param url The full URL to connect to.
      * @param connectTimeout Sets a specified timeout value, in milliseconds,
      * to be used when opening a communications link to the resource referenced
      * by this URLConnection. If the timeout expires before the connection can
      * be established, a java.net.SocketTimeoutException
      * is raised. A timeout of zero is interpreted as an infinite timeout.
      * Defaults to 5000 ms.
      * @param readTimeout If the timeout expires before there is data available
      * for read, a java.net.SocketTimeoutException is raised. A timeout of zero
      * is interpreted as an infinite timeout. Defaults to 5000 ms.
      * @param requestMethod Defaults to "GET". (Other methods have not been tested.)
      *
      * @example get("http://www.example.com/getInfo")
      * @example get("http://www.example.com/getInfo", 5000)
      * @example get("http://www.example.com/getInfo", 5000, 5000)
      */
    @throws(classOf[java.io.IOException])
    @throws(classOf[java.net.SocketTimeoutException])
    def get(url: String,
            connectTimeout: Int = 5000,
            readTimeout: Int = 5000,
            requestMethod: String = "GET") =
    {
        import java.net.{URL, HttpURLConnection}
        val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
        connection.setConnectTimeout(connectTimeout)
        connection.setReadTimeout(readTimeout)
        connection.setRequestMethod(requestMethod)
        val inputStream = connection.getInputStream
        val content = io.Source.fromInputStream(inputStream).mkString
        if (inputStream != null) inputStream.close
        content
    }
}