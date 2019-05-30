package ProjectX;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Ressl on 15.11.2017 09:22.
 */
public class Variables implements Serializable {
    long lastmil = 0;
    String time = "";
    boolean reset = false;
    boolean mute = true;
    boolean updated = false;
    boolean silent = false;
    Map<String,Long> buttons = new HashMap<>();

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

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isSilent() {

        return silent;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
