package cn.catver.qss

import cn.catver.qss.server.ServerClass
import cn.catver.qss.server.SpigotServer
import com.alibaba.fastjson2.JSONObject

fun main(){
    println("正在使用无极镜像！")
    val confRoot = JSONObject.parse(readFileFromInputStream(SpigotServer::class.java.classLoader.getResourceAsStream("conf.json")))
    val servers = confRoot.getJSONArray("servers")
    val ids = IntArray(servers.size)
    val serverConf : JSONObject
    while (true){
        for (i in 0 ..< servers.size){
            ids[i] = servers.getJSONObject(i).getInteger("id")
            println("${servers.getJSONObject(i).getInteger("id")}.${servers.getJSONObject(i).getString("name")}\n\t${servers.getJSONObject(i).getString("lore")}")
        }
        println("请输入服务端前的数字，或者输入非数字退出")
        print("输入：")
        val inp = readlnOrNull()
        if (inp == null) {
            println("已退出")
            return
        }
        val num = inp.toIntOrNull()
        if (num == null) {
            println("已退出")
            return
        }
        if (!ids.contains(num)){
            println("错误的输入")
            continue
        }
        println("选择成功！")
        serverConf = servers.getJSONObject(num)
        break
    }
    val clazz = Class.forName(serverConf.getString("serverClass"))
    val sclazz : ServerClass = clazz.newInstance() as ServerClass
    sclazz.run(serverConf)
}