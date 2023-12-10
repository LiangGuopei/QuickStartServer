package cn.catver.qss

import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.io.entity.EntityUtils
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Path
import kotlin.math.floor
import kotlin.math.roundToInt

fun readFileFromInputStream(inputStream: InputStream) : String{
    val br = BufferedReader(InputStreamReader(inputStream))
    val sb = StringBuilder()

    var temp:String?
    while (true){
        temp = br.readLine()
        if (temp == null) break
        sb.append(temp)
    }

    br.close()
    return sb.toString()
}

fun doGET(url:String) : String? {
    val httpGet = HttpGet(url)
    val httpClient: CloseableHttpClient = HttpClients.createDefault()
    httpClient.use {
        val response = httpClient.execute(httpGet)
        if (response.code == 200) {
            val httpEntity = response.entity
            return EntityUtils.toString(httpEntity)
        } else {
            return null
        }
    }
}

fun doDownload0(url : String,path:Path) : Boolean{
    val httpGet = HttpGet(url)
    val httpClient : CloseableHttpClient = HttpClients.createDefault()
    httpClient.use {
        val response = httpClient.execute(httpGet)
        if (response.code != 200) return false
        response.use {
            val sb = StringBuilder()
            for (i in 0..<100){
                sb.append(" ")
            }
            val inputStream = response.entity.content
            var now = 0L
            val size = response.entity.contentLength
            var jindu1 = 0
            inputStream.use {
                val fos = FileOutputStream(path.toFile())
                while (true){
                    val data = inputStream.read()
                    if (data == -1) break
                    fos.write(data)
                    now++
                    val jindu = floor((now.toDouble() / size.toDouble() * 100)).toInt()
                    sb.replace(jindu,jindu+1,"=")
                    if (jindu != jindu1){
                        print("\r[$jindu%][$sb]")
                    }
                    jindu1 = jindu

                }
                print("\n")
                fos.close()
            }
        }
    }
    return true
}