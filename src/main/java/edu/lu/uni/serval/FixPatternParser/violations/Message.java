package edu.lu.uni.serval.FixPatternParser.violations;


import javafx.util.Pair;

import java.io.Serializable;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class Message implements Serializable{
    Pair<Integer, String> first;
    Pair<Integer, String> second;
    Pair<Pair,Pair> p;

    public Pair<Pair, Pair> getPair() {
        return p;
    }

    public void setPair(Pair<Pair, Pair> p) {
        this.p = p;
    }



    public Message(Integer keyFirst, String valueFirst, Integer keySecond, String valueSecond ){
        first = new Pair<>(keyFirst,valueFirst);
        second = new Pair<>(keySecond,valueSecond);
        p = new Pair<>(first,second);
    }

}
