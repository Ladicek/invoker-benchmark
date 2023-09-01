package cz.ladicek.invoker.benchmark.weld;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Utils {
    static MethodHandle getMethodHandle(Method method, Class<?>... methodArgs) throws ReflectiveOperationException {
        MethodHandles.Lookup lookup = Modifier.isPublic(method.getModifiers())
                ? MethodHandles.publicLookup() : MethodHandles.lookup();

        MethodType methodType = MethodType.methodType(method.getReturnType(), methodArgs);
        if (Modifier.isStatic(method.getModifiers())) {
            return lookup.findStatic(method.getDeclaringClass(), method.getName(), methodType);
        } else {
            return lookup.findVirtual(method.getDeclaringClass(), method.getName(), methodType);
        }
    }

    static MethodHandle createMethodHandleFromTransformer(TransformerKind kind, Method transformerMethod,
            Class<?> transformationArgType) throws ReflectiveOperationException {
        MethodHandle result = getMethodHandle(transformerMethod, transformerMethod.getParameterTypes()[0]);

        // for input transformers, we might need to change return type to whatever the original method expects
        // for output transformers, we might need to change their input params
        // this enables transformers to operate on subclasses (input tf) or superclasses (output tf)
        if (kind.isInput() && !result.type().returnType().equals(transformationArgType)) {
            return result.asType(result.type().changeReturnType(transformationArgType));
        } else if (kind.isOutput() && result.type().parameterCount() > 0
                && !result.type().parameterType(0).equals(transformationArgType)) {
            return result.asType(result.type().changeParameterType(0, transformationArgType));
        } else {
            return result;
        }
    }

    enum TransformerKind {
        WRAPPER,
        INSTANCE,
        ARGUMENT,
        RETURN_VALUE,
        EXCEPTION;

        boolean isInput() {
            return this == INSTANCE || this == ARGUMENT;
        }

        boolean isOutput() {
            return this == RETURN_VALUE || this == EXCEPTION;
        }
    }
}
