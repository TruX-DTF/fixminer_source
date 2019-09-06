package edu.lu.uni.serval.fixminer.akka.ediff;

public interface KryoContext {

    byte[] serialze(Object obj);

    byte[] serialze(Object obj, int bufferSize);

    Object deserialze(Class clazz, byte[] serialized);
}
