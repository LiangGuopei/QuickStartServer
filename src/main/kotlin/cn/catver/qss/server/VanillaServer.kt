package cn.catver.qss.server

import cn.catver.qss.doDownload0
import cn.catver.qss.doGET
import com.alibaba.fastjson2.JSONObject
import java.io.File
import java.nio.file.Paths

class VanillaServer : ServerClass {
    override fun run(conf: JSONObject?) {
        println("你选择了："+conf!!.getString("name")+" 服务端")
        println("获取最新构建中")
        //构建获取 --start--
        val buildGETRESPONE = doGET(conf.getString("buildNameGetURL").format("release"))
        if (buildGETRESPONE == null){
            println("构建获取失败，退出")
            return
        }
        val build = JSONObject.parse(buildGETRESPONE).getJSONObject("data").getJSONArray("builds").getJSONObject(0).getString("core_version")
        println("最新构建：%s".format(build))
        //创建工作区 --start--
        if (!File("workspace").isDirectory){
            File("workspace").mkdir()
        }
        //删除旧端 --start--
        if (File("workspace/server.jar").isFile){
            File("workspace/server.jar").delete()
        }
        //下载服务端 --start--
        println("下载中，耐心等待")
//        val downloadResponse = doGET0(conf.getString("downloadURL").format(version,build))
        val path = Paths.get("workspace/server.jar")
        if (!doDownload0(conf.getString("downloadURL").format(build),path)){
            println("下载失败")
            return
        }
        println("下载成功！")
        println("启动指令：\"%s\" -jar workspace/server.jar nogui".format(Paths.get(System.getProperty("java.home"),"java").toString()))

    }
}