package cn.catver.qss.unit;

import cn.catver.qss.server.SpigotServer;
import com.alibaba.fastjson2.JSONObject;

public class Test {
    public static void main(String[] args) {
        JSONObject obj = JSONObject.parse("{\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"spigot\",\n" +
                "      \"lore\": \"paper的上游插件端\",\n" +
                "      \"serverClass\": \"cn.catver.qss.server.SpigotServer\",\n" +
                "      \"versionGetURL\": \"https://download.fastmirror.net/api/v3/Spigot/\",\n" +
                "      \"buildNameGetURL\": \"https://download.fastmirror.net/api/v3/Spigot/%s\",\n" +
                "      \"downloadURL\": \"https://download.fastmirror.net/download/Spigot/%s/%s\"\n" +
                "    }");
        new SpigotServer().run(obj);
    }
}
