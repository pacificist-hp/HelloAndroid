# Bridge
The Bridge is a simple interpreter which parses a special script language with a custom syntax. We can register Java functions in the Bridge, and then call them in a script dynamically. It looks like a bridge connecting Java and Script.

### identifier
A symbol to name language entities (variable, class, function...)

### literal
A letter or symbol that stands for itself as opposed to a feature, function, or entity associated with it in a programming language.
```
int a = 10 // 'a' is a variable, 10 is a int literal
string b = "hello" // "hello" is a string literal
```

### expression
An expression has a runtime value. It evaluates to a value only. For example, the conditional expression `condition ? expr1 : expr2` has a value of `expr1` or `expr2`.

### Statement
A statement contains executable codes. It often contains one or more expressions, but an expression can not directly contain a statement.  
Statements do not have runtime values. For example, an `if-else` statement has no value.


