package edu.lu.uni.serval.fixminer.akka.ediff;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.UnsafeInput;
import com.esotericsoftware.kryo.io.UnsafeOutput;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.objenesis.strategy.SerializingInstantiatorStrategy;


public class DefaultKryoContext implements KryoContext{

    private static final int DEFAULT_BUFFER = 1024 * 100;

    private KryoPool pool;

    public static KryoContext newKryoContextFactory()
    {
        return new DefaultKryoContext();
    }

    private DefaultKryoContext()
    {
        KryoFactory factory = new KryoFactoryImpl();

        pool = new KryoPool.Builder(factory).softReferences().build();
    }

    private static class KryoFactoryImpl implements KryoFactory
    {
        @Override
        public Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new SerializingInstantiatorStrategy()));
            return kryo;
        }
    }


    @Override
    public byte[] serialze(Object obj)
    {
        return serialze(obj, DEFAULT_BUFFER);
    }

    @Override
    public byte[] serialze(Object obj, int bufferSize)
    {
        ByteArrayOutputStream base = new ByteArrayOutputStream();
        Output output = new Output(base, bufferSize);

        Kryo kryo = pool.borrow();

        kryo.writeObject(output, obj);

        output.flush();
        byte[] serialized = base.toByteArray();
        output.close();

//        byte[] serialized = output.toBytes();

        pool.release(kryo);

        return serialized;
    }

    @Override
    public Object deserialze(Class clazz, byte[] serialized)
    {
        Object obj;

        Kryo kryo = pool.borrow();

        Input input = new Input(serialized);
        obj = kryo.readObject(input, clazz);

        pool.release(kryo);

        return obj;
    }

}