package cz.ladicek.invoker.benchmark.reflection;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;

import java.lang.reflect.Method;

// this is a hypothetical reflective implementation of a direct invoker (without transformations)
public class ReflectionDirectInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new ReflectionDirectInvoker();

    private final Method method;

    private ReflectionDirectInvoker() {
        try {
            this.method = InvokableBean.class.getDeclaredMethod("hello", int.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        try {
            return (String) method.invoke(instance, arguments);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
