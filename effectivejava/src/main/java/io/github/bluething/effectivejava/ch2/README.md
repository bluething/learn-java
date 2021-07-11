### Chapter 2. Creating and Destroying Objects

#### Item 1: Consider static factory methods instead of constructors

- Unlike constructors, they have names. Method name express the object returned and of course express the intent clearly.  
Example, BigInteger(int numBits, Random rnd) vs BigInteger.probablePrime(int bitLength, Random rnd)  
Constructor limited by signature (parameter types). So if we want additional constructor (like generating prime number above),
we will create other constructor with different parameter types (Random rnd). The user of such an API need to read the documentation before use the class.

- Unlike constructors, they are not required to create a new object each time they’re invoked.  
This allows immutable classes or to cache instances they are constructed.  
Example,  
`
public static Boolean valueOf(boolean b) {
    return (b ? TRUE : FALSE);
}
 `  
 This technique is similar to the _Flyweight_ pattern.
 This ability (cerate object -> cache -> return same object) allows classes to maintain strict control over what instances exist at any time.  
 Classes that do this are said to be _instance-controlled_.  
 Why instance-controlled good?  
  - Singleton guarantee  
  - Allows immutable, a.equals(b) if only if a == b.
  
- Unlike constructors, they can return an object of any subtype of their return type.  
Example, Collections framework.

- The class of the returned object can vary from call to call as a function of the input parameters.  
Example, EnumSet.  
They don't have constructor (only static factory method). Return an instance of one of two subclasses, depending on the size of the underlying enum type.

- The class of the returned object need not exist when the class containing the method is written.  
Example, JDBC API.  
Provider (ex Mysql) will implement the service , system makes implementation available to the client, decoupling the clients from the implementations. We call this _service provider framework_.  
There are 3 components of service provider framework:  
  - Service interface. Example, Connection class.
  - Provider registration. Example, DriverManager.registerDriver().
  - Service access API. Example, DriverManager.getConnection().  
Othe example _Dependency Injection_ and _Bridge pattern_.

- Static factory methods is that classes without public or protected constructors cannot be subclassed.  
This is good, we force to implement composition instead of inheritance and creating immutable class.

- They are hard for programmers to find.  
We need to read documentation before use the class. We can use convention name to ease us when using the class.

###### Item 2: Consider a builder when faced with many constructor parameters

###### Item 3: Enforce the singleton property with a private constructor or an enum type

###### Item 4: Enforce noninstantiability with a private constructor

###### Item 5: Prefer dependency injection to hardwiring resources

###### Item 6: Avoid creating unnecessary objects

###### Item 7: Eliminate obsolete object references

###### Item 8: Avoid finalizers and cleaners

###### Item 9: Prefer try-with-resources to try-finally