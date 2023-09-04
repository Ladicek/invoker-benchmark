package cz.ladicek.invoker.benchmark.weld;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

// this is how Weld would create a direct invoker (without transformations)
//
// NOTE: this is actually a simplification of what the Weld prototype does!
public class WeldDirectInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new WeldDirectInvoker();

    private final MethodHandle mh;

    private WeldDirectInvoker() {
        try {
            Method method = InvokableBean.class.getDeclaredMethod("hello", int.class);

            MethodHandle mh = Utils.getMethodHandle(method, method.getParameterTypes());

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
