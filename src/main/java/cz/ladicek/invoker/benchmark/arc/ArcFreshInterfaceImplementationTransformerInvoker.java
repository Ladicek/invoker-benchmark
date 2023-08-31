package cz.ladicek.invoker.benchmark.arc;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;
import cz.ladicek.invoker.benchmark.cdi.Transformer;

// this is how ArC would generate an invoker with a return value transformation
// in case the transformer is an implementation of the `Transformer` interface
// and a fresh new instance of the transformer is created for each invocation
public class ArcFreshInterfaceImplementationTransformerInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new ArcFreshInterfaceImplementationTransformerInvoker();

    private ArcFreshInterfaceImplementationTransformerInvoker() {
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        Transformer<String, String> returnValueTransformer = new InvokableBean.Transform();
        return returnValueTransformer.transform(instance.hello((Integer) arguments[0]));
    }
}
