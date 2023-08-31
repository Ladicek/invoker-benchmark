package cz.ladicek.invoker.benchmark.arc;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;

// this is how ArC would generate a direct invoker (without transformations)
public class ArcDirectInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new ArcDirectInvoker();

    private ArcDirectInvoker() {
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        return instance.hello((Integer) arguments[0]);
    }
}
