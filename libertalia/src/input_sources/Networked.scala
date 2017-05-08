package input_sources

import java.util

import libertalia.{Channel, OutputManager}
import org.apache.http.{NameValuePair}
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.{ BasicNameValuePair}


trait Networked {
    val HOST = "http://rutsubo-thewindwhispers.rhcloud.com"

    def post(operation : String, parameters : Map[String, String]) : String = {
      val post = new HttpPost(HOST + "/" + operation)
      var nameValuePairs = new util.ArrayList[NameValuePair]()
      parameters.foreach { case (key, value) =>
        nameValuePairs.add(new BasicNameValuePair(key, value))
      }
      post.setEntity(new UrlEncodedFormEntity(nameValuePairs))
      post.setHeader("Content-type", "application/x-www-form-urlencoded")

      val post_response = (new DefaultHttpClient).execute(post)
      OutputManager.print(Channel.Debug, "Networked Bot: " + post_response)
      return post_response.getEntity.getContent.toString
    }

    def get(operation : String, parameters : Map[String, String]) : String = {
      val query_string = "?" + parameters.map{ case (key, value) => key + "=" + value}.mkString("&")
      val get_request = new HttpGet(HOST + "/" + operation + query_string)
      get_request.addHeader("Content-type", "application/x-www-form-urlencoded")
      val get_response = (new DefaultHttpClient).execute(get_request)
      OutputManager.print(Channel.Debug, "Networked Bot: " + get_response)

      val inputStream = get_response.getEntity.getContent
      val content = io.Source.fromInputStream(inputStream).getLines.mkString
      inputStream.close

      return content
    }
}
