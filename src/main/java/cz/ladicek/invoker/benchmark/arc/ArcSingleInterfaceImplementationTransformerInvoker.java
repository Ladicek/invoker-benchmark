package cz.ladicek.invoker.benchmark.arc;

import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;
import cz.ladicek.invoker.benchmark.cdi.Transformer;

// this is how ArC would generate an invoker with a return value transformation
// in case the transformer is an implementation of the `Transformer` interface
// and there's a single instance of the transformer shared for all invocations
public class ArcSingleInterfaceImplementationTransformerInvoker implements Invoker<InvokableBean, String> {
    public static final Invoker<InvokableBean, String> INSTANCE = new ArcSingleInterfaceImplementationTransformerInvoker();

    private final Transformer<String, String> returnValueTransformer = new InvokableBean.Transform();

    private ArcSingleInterfaceImplementationTransformerInvoker() {
    }

    @Override
    public String invoke(InvokableBean instance, Object[] arguments) {
        return returnValueTransformer.transform(instance.hello((Integer) arguments[0]));
    }
}
