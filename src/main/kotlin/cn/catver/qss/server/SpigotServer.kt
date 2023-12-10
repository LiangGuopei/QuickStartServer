package cn.catver.qss.server

import cn.catver.qss.doDownload0
import cn.catver.qss.doGET
import com.alibaba.fastjson2.JSONObject
import java.io.File
import java.nio.file.Paths

class SpigotServer : ServerClass {
    override fun run(conf: JSONObject?) {
        println("你选择了："+conf!!.getString("name")+" 服务端")
        println("获取版本中。")
        //版本获取 --start--
        val versionGETRESPONE = doGET(conf.getString("versionGetURL"))
        if (versionGETRESPONE == null){
            println("版本获取失败，退出")
            return
        }
        val versionJSON = JSONObject.parse(versionGETRESPONE)
        val version : String?
        while (true){
            val allowVersion = versionJSON.getJSONObject("data").getJSONArray("mc_versions")
            println("可用版本")
            for (i in 0..<allowVersion.size){
                if ((i+1) % 6 == 0){
                    println("\t\t%s".format(allowVersion.getString(i)))
                }else{
                    print("\t\t%s".format(allowVersion.getString(i)))
                }
            }
            println("\t\t输入q以取消")
            val inp = readlnOrNull() ?: continue
            if (inp == "q") return
            if (!allowVersion.contains(inp)){
                println("错误的输入")
                continue
            }
            version = inp
            println("你选择了%s 版本".format(version))
            break
        }
        //构建获取 --start--
        println("获取最新构建中")
        val buildGETRESPONE = doGET(conf.getString("buildNameGetURL").format(version))
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
        if (!doDownload0(conf.getString("downloadURL").format(version,build),path)){
            println("下载失败")
            return
        }
        println("下载成功！")
        println("启动指令：\"%s\" -jar workspace/server.jar nogui".format(Paths.get(System.getProperty("java.home"),"java").toString()))

    }
}