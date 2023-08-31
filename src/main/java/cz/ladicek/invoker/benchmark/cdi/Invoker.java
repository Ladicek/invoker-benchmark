package cz.ladicek.invoker.benchmark.cdi;

// the CDI `Invoker` interface
public interface Invoker<T, R> {
    R invoke(T instance, Object[] arguments);
}
