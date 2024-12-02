package org.automaticalechoes.simplesign.api;


import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import org.automaticalechoes.simplesign.Constants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Config {
    private static final Splitter OPTION_SPLITTER = Splitter.on(':').limit(2);
    private static final String CONFIG_FILE = "config\\echoes_mods_client_config.txt";
    static final String SSI$KEY_PING = "simplesign_key_ping";
    static final String SSI$AUTO_RECEIVE = "simplesign_auto_receive";
    static final String SSI$AUTO_RECEIVE_KEEP_TIME = "simplesign_auto_receive_keep_time";
    static final String SSI$AUTO_RECEIVE_DISTANCE = "simplesign_auto_receive_distance";
    static final String SSI$KEY_MARK = "simplesign_key_mark";
    static final String SSI$KEY_CLEAR_MARK = "simplesign_key_clear_mark";
    static final String SSI$KEY_REMOVE_MARK = "simplesign_key_remove_mark";
    static Boolean KEY_PING = true;
    static Boolean AUTO_RECEIVE = true;
    static Integer AUTO_RECEIVE_KEEP_TIME = 10;
    static Integer AUTO_RECEIVE_DISTANCE = 200;
    static Boolean KEY_MARK = true;
    static Boolean KEY_CLEAR_MARK = true;
    static Boolean KEY_REMOVE_MARK = true;

    public static Integer AutoReceiveKeepTime(){
        return AUTO_RECEIVE_KEEP_TIME;
    }

    public static Integer AutoReceiveDistance(){
        return AUTO_RECEIVE_DISTANCE;
    }

    public static boolean AutoReceive() {
        return AUTO_RECEIVE;
    }

    public static boolean KeyPing() {
        return KEY_PING;
    }

    public static boolean KeyMark() {
        return KEY_MARK;
    }

    public static boolean KeyClearMark() {
        return KEY_CLEAR_MARK;
    }

    public static boolean KeyRemoveMark() {
        return KEY_REMOVE_MARK;
    }

    public static void save(){
        File configDir = new File(Minecraft.getInstance().gameDirectory, CONFIG_FILE);
        save(configDir);
    }

    public static void save(File configDir) {
        try {
            final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(configDir), StandardCharsets.UTF_8));
            try {
                printWriter.println(SSI$AUTO_RECEIVE + ":" + AUTO_RECEIVE);
                printWriter.println(SSI$AUTO_RECEIVE_KEEP_TIME + ":" + AUTO_RECEIVE_KEEP_TIME);
                printWriter.println(SSI$AUTO_RECEIVE_DISTANCE + ":" + AUTO_RECEIVE_DISTANCE);
                printWriter.println(SSI$KEY_PING + ":" + KEY_PING);
                printWriter.println(SSI$KEY_MARK + ":" + KEY_MARK);
                printWriter.println(SSI$KEY_CLEAR_MARK + ":" + KEY_CLEAR_MARK);
                printWriter.println(SSI$KEY_REMOVE_MARK + ":" + KEY_REMOVE_MARK);
            } catch (Throwable var5) {
                try {
                    printWriter.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
                throw var5;
            }
            printWriter.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public static void load() {
        File configDir = new File(Minecraft.getInstance().gameDirectory, CONFIG_FILE);
        try {
            if (!configDir.exists()) {
                save(configDir);
                return;
            }

            CompoundTag compoundTag = new CompoundTag();
            BufferedReader bufferedReader = Files.newReader(configDir, Charsets.UTF_8);

            try {
                bufferedReader.lines().forEach((string) -> {
                    try {
                        Iterator<String> iterator = OPTION_SPLITTER.split(string).iterator();
                        compoundTag.putString(iterator.next(), (String)iterator.next());
                    } catch (Exception var3) {
                        Constants.LOG.warn("Skipping bad option: {}", string);
                    }

                });
            } catch (Throwable var6) {
                try {
                    bufferedReader.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }
            bufferedReader.close();
            if (compoundTag.contains(SSI$AUTO_RECEIVE)) {
                AUTO_RECEIVE = isTrue(compoundTag.getString(SSI$AUTO_RECEIVE));
            }
            if (compoundTag.contains(SSI$KEY_PING)) {
                KEY_PING = isTrue(compoundTag.getString(SSI$KEY_PING));
            }
            if (compoundTag.contains(SSI$KEY_MARK)) {
                KEY_MARK = isTrue(compoundTag.getString(SSI$KEY_MARK));
            }
            if (compoundTag.contains(SSI$KEY_CLEAR_MARK)) {
                KEY_CLEAR_MARK = isTrue(compoundTag.getString(SSI$KEY_CLEAR_MARK));
            }
            if(compoundTag.contains(SSI$KEY_REMOVE_MARK)) {
                KEY_REMOVE_MARK = isTrue(compoundTag.getString(SSI$KEY_REMOVE_MARK));
            }
            if(compoundTag.contains(SSI$AUTO_RECEIVE_KEEP_TIME)){
                AUTO_RECEIVE_KEEP_TIME = Integer.getInteger(compoundTag.getString(SSI$AUTO_RECEIVE_KEEP_TIME));
            }
            if (compoundTag.contains(SSI$AUTO_RECEIVE_DISTANCE)) {
                AUTO_RECEIVE_DISTANCE = Integer.getInteger(compoundTag.getString(SSI$AUTO_RECEIVE_DISTANCE));
            }
            KeyMapping.resetMapping();
        } catch (Exception var7) {
            Constants.LOG.error("Failed to load options", var7);
        }

    }

    static boolean isTrue(String string) {
        return "true".equals(string);
    }
}
