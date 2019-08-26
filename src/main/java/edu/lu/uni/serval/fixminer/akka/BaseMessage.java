package edu.lu.uni.serval.fixminer.akka;

/**
 * Created by anilkoyuncu on 18/09/2018.
 */
public class BaseMessage {

    private long SECONDS_TO_WAIT;
    private int id;
    private int threadPoolSize;


    public BaseMessage(int id,Long timeout) {
        this.id = id;
        this.SECONDS_TO_WAIT = timeout;
//        this.threadPoolSize = threadPoolSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public long getSECONDS_TO_WAIT() {
        return SECONDS_TO_WAIT;
    }

    public void setSECONDS_TO_WAIT(long SECONDS_TO_WAIT) {
        this.SECONDS_TO_WAIT = SECONDS_TO_WAIT;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }


}
