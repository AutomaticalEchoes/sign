package automaticalechoes.simplesign.simplesign.api;

import automaticalechoes.simplesign.simplesign.SimpleSign;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class Config {
    private static final Splitter OPTION_SPLITTER = Splitter.on(':').limit(2);
    private static final String CONFIG_FILE = "config\\simplesign_client_config.txt";
    static final Map<String, Value<?>> VALUES = new HashMap<>();
    static final String SSI$KEYMAPPING_MARK_DEFAULT = "ssi_keymapping_mark";
    static final String SSI$KEYMAPPING_GET_MARK = "ssi_keymapping_get_mark";
    static final String SSI$KEYMAPPING_REMOVE_MARK = "ssi_keymapping_remove_mark";
    static final String SSI$KEYMAPPING_CLEAR_MARK = "ssi_keymapping_clear_mark";
    static final String SSI$KEYMAPPING_SIGN_SLOT = "ssi_keymapping_sign_slot";

    static Value<Boolean> SSI$KEY_MARK_DEFAULT = Value.createBoolean(SSI$KEYMAPPING_MARK_DEFAULT);
    static Value<Boolean> SSI$KEY_GET_MARK = Value.createBoolean(SSI$KEYMAPPING_GET_MARK);
    static Value<Boolean> SSI$KEY_REMOVE_MARK = Value.createBoolean(SSI$KEYMAPPING_REMOVE_MARK);
    static Value<Boolean> SSI$KEY_CLEAR_MARK = Value.createBoolean(SSI$KEYMAPPING_CLEAR_MARK);
    static Value<Boolean> SSI$KEY_SIGN_SLOT = Value.createBoolean(SSI$KEYMAPPING_SIGN_SLOT);

    static class Value<T>{
        String name;
        T value;
        Function<String, T> codec;

        public Value(String name, T value, Function<String, T> codec) {
            this.name = name;
            this.value = value;
            this.codec = codec;
            VALUES.put(this.name, this);
        }

        public void fromTag(CompoundTag tag) {
            if (tag.contains(name)){
                this.value = codec.apply(tag.getString(name));
            }
        }

        public static Value<Boolean> createBoolean(String name) {
            return new Value<>(name, true, Config::isTrue);
        }

    }

    public static Boolean getSsi$keyClearMark() {
        return SSI$KEY_CLEAR_MARK.value;
    }

    public static Boolean getSsi$keyGetMark() {
        return SSI$KEY_GET_MARK.value;
    }

    public static Boolean getSsi$keyMarker() {
        return SSI$KEY_MARK_DEFAULT.value;
    }

    public static Boolean getSsi$keyRemoveMark() {
        return SSI$KEY_REMOVE_MARK.value;
    }

    public static Boolean getSsi$keySignSlot() {
        return SSI$KEY_SIGN_SLOT.value;
    }

    public static void save(){
        File configDir = new File(Minecraft.getInstance().gameDirectory, CONFIG_FILE);
        save(configDir);
    }

    public static void save(File configDir) {
        try {
            final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(configDir), StandardCharsets.UTF_8));

            try {
                for (Map.Entry<String, Value<?>> entry : VALUES.entrySet()) {
                    printWriter.println(entry.getKey() + ":" + entry.getValue().value);
                }
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
                        compoundTag.putString(iterator.next(), iterator.next());
                    } catch (Exception var3) {
                        SimpleSign.LOGGER.warn("Skipping bad option: {}", string);
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
            for (Map.Entry<String, Value<?>> entry : VALUES.entrySet()) {
                if (compoundTag.contains(entry.getKey())) {
                    entry.getValue().fromTag(compoundTag);
                }
            }

            KeyMapping.resetMapping();
        } catch (Exception var7) {
            SimpleSign.LOGGER.error("Failed to load options", var7);
        }

    }

    static boolean isTrue(String string) {
        return "true".equals(string);
    }
}
