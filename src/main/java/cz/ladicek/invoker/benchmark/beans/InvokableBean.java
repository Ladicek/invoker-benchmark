package cz.ladicek.invoker.benchmark.beans;

import cz.ladicek.invoker.benchmark.cdi.Transformer;
import org.openjdk.jmh.infra.Blackhole;

// a hypothetical CDI bean with an invokable method
// the hypothetical scope is `@Dependent`, because we don't want to measure the overhead of client proxies
public class InvokableBean {
    public String hello(int param) {
        Blackhole.consumeCPU(500);
        return "hello";
    }

    // a static method transformer that would be used when transformers are referred to using (Class, String) pairs
    public static String transform(String value) {
        Blackhole.consumeCPU(100);
        return value;
    }

    // an interface implementation transformer that would be used when transformers are referred to using Class objects
    public static class Transform implements Transformer<String, String> {
        @Override
        public String transform(String value) {
            Blackhole.consumeCPU(100);
            return value;
        }
    }
}
