package cz.ladicek.invoker.benchmark;

import cz.ladicek.invoker.benchmark.arc.ArcDirectInvoker;
import cz.ladicek.invoker.benchmark.arc.ArcFreshInterfaceImplementationTransformerInvoker;
import cz.ladicek.invoker.benchmark.arc.ArcMethodTransformerInvoker;
import cz.ladicek.invoker.benchmark.arc.ArcSingleInterfaceImplementationTransformerInvoker;
import cz.ladicek.invoker.benchmark.beans.InvokableBean;
import cz.ladicek.invoker.benchmark.cdi.Invoker;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.Throughput)
@Fork(5)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 5, time = 5)
@State(Scope.Benchmark)
public class InvokerBenchmark {
    private InvokableBean bean;

    private Invoker<InvokableBean, String> arcDirectInvoker;
    private Invoker<InvokableBean, String> arcMethodTransformerInvoker;
    private Invoker<InvokableBean, String> arcSingleInterfaceImplementationTransformerInvoker;
    private Invoker<InvokableBean, String> arcFreshInterfaceImplementationTransformerInvoker;

    @Setup
    public void setup() {
        bean = new InvokableBean();

        arcDirectInvoker = ArcDirectInvoker.INSTANCE;
        arcMethodTransformerInvoker = ArcMethodTransformerInvoker.INSTANCE;
        arcSingleInterfaceImplementationTransformerInvoker = ArcSingleInterfaceImplementationTransformerInvoker.INSTANCE;
        arcFreshInterfaceImplementationTransformerInvoker = ArcFreshInterfaceImplementationTransformerInvoker.INSTANCE;
    }

    @Benchmark
    public String baseline() {
        return bean.hello(42);
    }

    @Benchmark
    public String arcDirectInvoker() {
        return arcDirectInvoker.invoke(bean, new Object[]{42});
    }

    @Benchmark
    public String arcMethodTransformerInvoker() {
        return arcMethodTransformerInvoker.invoke(bean, new Object[]{42});
    }

    @Benchmark
    public String arcSingleInterfaceImplementationTransformerInvoker() {
        return arcSingleInterfaceImplementationTransformerInvoker.invoke(bean, new Object[]{42});
    }

    @Benchmark
    public String arcFreshInterfaceImplementationTransformerInvoker() {
        return arcFreshInterfaceImplementationTransformerInvoker.invoke(bean, new Object[]{42});
    }
}