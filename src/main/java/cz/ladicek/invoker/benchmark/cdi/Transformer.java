package cz.ladicek.invoker.benchmark.cdi;

// a hypothetical CDI `Transformer` interface, if we decide to use this strategy of writing transformers
public interface Transformer<S, T> {
    T transform(S value);
}
