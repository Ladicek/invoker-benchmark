package cz.ladicek.invoker.benchmark.reflection;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;
import cz.ladicek.invoker.benchmark.cdi.Transformer;

import java.lang.reflect.Method;

// this is a hypothetical reflective implementation of an invoker with a return value transformation
// in case the transformer is an implementation of the `Transformer` interface
// and there's a single instance of the transformer shared for all invocations,
public class ReflectionSingleInterfaceImplementationTransformerInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new ReflectionSingleInterfaceImplementationTransformerInvoker();

    private final Method method;
    private final Transformer<Object, Object> returnValueTransformer;

    private ReflectionSingleInterfaceImplementationTransformerInvoker() {
        try {
            this.method = InvokableBean.class.getDeclaredMethod("hello", int.class);
            this.returnValueTransformer = (Transformer) InvokableBean.Transform.class.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        try {
            Object result = method.invoke(instance, arguments);
            return (String) returnValueTransformer.transform(result);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
