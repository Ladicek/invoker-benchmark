package cz.ladicek.invoker.benchmark.arc;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;

// this is how ArC would generate an invoker with a return value transformation
// in case the transformer is a static method
public class ArcMethodTransformerInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new ArcMethodTransformerInvoker();

    private ArcMethodTransformerInvoker() {
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        return InvokableBean.transform(instance.hello((Integer) arguments[0]));
    }
}
