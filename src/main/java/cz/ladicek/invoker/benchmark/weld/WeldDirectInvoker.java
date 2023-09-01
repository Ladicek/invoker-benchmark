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

    private final Method method;
    private final MethodHandle mh;

    private WeldDirectInvoker() {
        try {
            this.method = InvokableBean.class.getDeclaredMethod("hello", int.class);
            this.mh = Utils.getMethodHandle(method, method.getParameterTypes());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        List<Object> args = new ArrayList<>(arguments.length + 1);
        for (int i = 0; i < arguments.length; i++) {
            args.add(i, arguments[i]);
        }

        // for non-static methods, first arg is the instance to invoke it on
        if (!Modifier.isStatic(method.getModifiers())) {
            args.add(0, instance);
        }

        try {
            return (String) mh.invokeWithArguments(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
