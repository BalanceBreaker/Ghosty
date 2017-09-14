package ProjectX;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Ressl on 02.04.2017 15:56.
 */
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    boolean hidden;
    boolean autostart;
    boolean startupNotification;
    boolean unkillable;
    boolean antiAV;
    boolean troll;
    boolean camera;
    boolean autoupdate;
    boolean taskhide;
    boolean alive;
    String token;
    String name;
    String admin;
    boolean silent;
    String password;
    long lastmil = 0;
    String time = "";
    boolean reset = false;
    boolean mute = true;
    Map<String,Long> buttons = new HashMap<>();

    public Config(boolean selected, boolean selected1, boolean selected2, boolean selected3, boolean selected4, boolean selected5, boolean selected6, boolean selected7, boolean selected8, boolean selected9, String text, String text1, boolean selected10, String text2) {
        this.hidden = selected;
        this.autostart = selected1;
        this.startupNotification = selected2;
        this.unkillable = selected3;
        this.antiAV = selected4;
        this.troll = selected5;
        this.camera = selected6;
        this.autoupdate = selected7;
        this.taskhide = selected8;
        this.alive = selected9;
        this.token = text;
        this.name = text1;
        this.silent = selected10;
        this.password = text2;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isAutostart() {
        return autostart;
    }

    public boolean isStartupNotification() {
        return startupNotification;
    }

    public boolean isUnkillable() {
        return unkillable;
    }

    public boolean isAntiAV() {
        return antiAV;
    }

    public boolean isTroll() {
        return troll;
    }

    public boolean isCamera() {
        return camera;
    }

    public boolean isAutoupdate() {
        return autoupdate;
    }

    public boolean isTaskhide() {
        return taskhide;
    }

    public boolean isAlive() {
        return alive;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isSilent() {

        return silent;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getLastmil() {
        return lastmil;
    }

    public void setLastmil(long lastmil) {
        this.lastmil = lastmil;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, Long> getButtons() {
        return buttons;
    }

    public void setButtons(Map<String, Long> buttons) {
        this.buttons = buttons;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }
}
