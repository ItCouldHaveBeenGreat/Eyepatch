package input_sources

import java.io.InputStreamReader
import java.util
import java.util.Scanner

import com.google.common.io.CharStreams
import libertalia.{Channel, OutputManager}
import org.apache.http.{Header, NameValuePair}
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.{BasicHeader, BasicNameValuePair}

import scala.collection.mutable.ArrayBuffer

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
      val get = new HttpGet(HOST + "/" + operation)
      var nameValuePairs = ArrayBuffer[Header]()
      parameters.foreach { case (key, value) =>
        nameValuePairs += new BasicHeader(key, value)
      }
      nameValuePairs += new BasicHeader("Content-type", "application/x-www-form-urlencoded")
      get.setHeaders(nameValuePairs.toArray)

      val get_response = (new DefaultHttpClient).execute(get)
      OutputManager.print(Channel.Debug, "Networked Bot: " + get_response)
      return CharStreams.toString(new InputStreamReader(get_response.getEntity.getContent, get_response.getEntity.getContentEncoding.getName))
    }
}
