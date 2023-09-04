package cz.ladicek.invoker.benchmark.reflection;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;

import java.lang.reflect.Method;

// this is a hypothetical reflective implementation of an invoker with a return value transformation
// in case the transformer is a static method
public class ReflectionMethodTransformerInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new ReflectionMethodTransformerInvoker();

    private final Method method;
    private final Method returnValueTransformer;

    private ReflectionMethodTransformerInvoker() {
        try {
            this.method = InvokableBean.class.getDeclaredMethod("hello", int.class);
            this.returnValueTransformer = InvokableBean.class.getDeclaredMethod("transform", String.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        try {
            Object result = method.invoke(instance, arguments);
            return (String) returnValueTransformer.invoke(null, result);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
