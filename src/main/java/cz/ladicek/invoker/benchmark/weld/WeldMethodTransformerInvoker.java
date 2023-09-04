package cz.ladicek.invoker.benchmark.weld;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

// this is how Weld would create an invoker with a return value transformation
// in case the transformer is a static method
//
// NOTE: this is actually a simplification of what the Weld prototype does!
public class WeldMethodTransformerInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new WeldMethodTransformerInvoker();

    private final MethodHandle mh;

    private WeldMethodTransformerInvoker() {
        try {
            Method method = InvokableBean.class.getDeclaredMethod("hello", int.class);
            Method transformer = InvokableBean.class.getDeclaredMethod("transform", String.class);

            MethodHandle mh = Utils.getMethodHandle(method, method.getParameterTypes());
            mh = MethodHandles.filterReturnValue(mh, Utils.createMethodHandleFromTransformer(
                    Utils.TransformerKind.RETURN_VALUE, transformer, mh.type().returnType()));

            if (Modifier.isStatic(method.getModifiers())) {
                MethodHandle invoker = MethodHandles.spreadInvoker(mh.type(), 0);
                invoker = MethodHandles.insertArguments(invoker, 0, mh);
                invoker = MethodHandles.dropArguments(invoker, 0, Object.class);
                this.mh = invoker;
            } else {
                MethodHandle invoker = MethodHandles.spreadInvoker(mh.type(), 1);
                invoker = MethodHandles.insertArguments(invoker, 0, mh);
                this.mh = invoker;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        try {
            return (String) mh.invoke(instance, arguments);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
