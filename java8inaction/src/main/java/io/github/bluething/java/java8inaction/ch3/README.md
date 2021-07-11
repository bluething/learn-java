Where we can use lambdas? In the context of a functional interface.

What is functional interface? A functional interface is an interface that specifies exactly one abstract method.
Interface can have default method, how about it? An interface still a functional interface _if it has only one abstract method_, even they have many default method.

Functional interface signature

| Functional interface | Function descriptor |
| -------------------- | ------------------- |
| Predicate<T>         | T -> boolean        |
| Consumer<T>          | T -> void           |
| Function<T, R>       | T -> R              |
| Supplier<T>          | () -> T             |
| UnaryOperator<T>     | T -> T              |
| BinaryOperator<T>    | (T, T) -> T         |
| BiPredicate<L, R>    | (L, R) -> boolean   |
| BiConsumer<T, U>     | (T, U) -> void      |
| BiFunction<T, U, R>  | (T, U) -> R         |

Lambda expression can provide the implementation of abstract method of functional interface directly inline and treat the whole expression as an instance of a functional interface (like a concrete implementation).

Java8 have several new functional interfaces inside the java.util.function package.

None of the functional interfaces allow for a checked exception to be thrown. You have two options:
- Define your own functional interface that declares the checked exception
- Wrap the lambda with a try/catch block.

_Execute around pattern_, for example when we want to query to database. We need to open a resource, do some processing on it, and then close the resource. 