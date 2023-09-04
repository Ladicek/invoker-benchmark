package cz.ladicek.invoker.benchmark.reflection;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;
import cz.ladicek.invoker.benchmark.cdi.Transformer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// this is a hypothetical reflective implementation of an invoker with a return value transformation
// in case the transformer is an implementation of the `Transformer` interface
// and a fresh new instance of the transformer is created for each invocation
public class ReflectionFreshInterfaceImplementationTransformerInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new ReflectionFreshInterfaceImplementationTransformerInvoker();

    private final Method method;
    private final Constructor<Transformer<Object, Object>> returnValueTransformer;

    private ReflectionFreshInterfaceImplementationTransformerInvoker() {
        try {
            this.method = InvokableBean.class.getDeclaredMethod("hello", int.class);
            this.returnValueTransformer = (Constructor) InvokableBean.Transform.class.getConstructor();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        try {
            Object result = method.invoke(instance, arguments);
            Transformer<Object, Object> transformer = returnValueTransformer.newInstance();
            return (String) transformer.transform(result);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
