# Spock
Spock is a testing framework that comes bundled with support for all the standard stuff you're used to:
* JUnit support
* Mocking
* Assertions
* BDD

on top of that Spock also makes your verbose and boilerplate heavy tests elegant and simple through the power of [Groovy](http://www.groovy-lang.org/). Check this out:
```Groovy
   def "Should create bank account successfully"() {
        when:
        service.createBankAccount(UUID)

        then:
        notThrown(IllegalArgumentException)
    }
```
It also provides awesome feedback on test failures like this:
```
account.uuid == "444b378f-bf1c-0031-86e7-ca27c77a7414"
|       |    |
|       |    false
|       |    4 differences (88% similarity)
|       |    444b378f-bf1c-(4878)-86e7-ca27c77a7414
|       |    444b378f-bf1c-(0031)-86e7-ca27c77a7414
|       444b378f-bf1c-4878-86e7-ca27c77a7414
```

## Getting started

### Prerequsite for the sample app
To be able to use the sample app in this repo you need to have [Git](http://git-scm.com/), [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Gradle](http://www.gradle.org/) installed. Then you can clone the repo with `git clone https://github.com/erikryverling/spock-demo.git` and then run `gradle test` in the root folder to run the tests.

### Method naming
One of the first thing that you might notice is that methods names can be strings. `ThatMeansTheEndOfLongCamelCaseMethodNames`. To create a test for creating a bank account sucessfully, just define a method as

```Groovy
def "Should create bank account successfully"() {
}
```

### Fixture methods
You have all the common life cycle methods you're used to
```Groovy
def setup() {}        // run before every feature method
def cleanup() {}      // run after every feature method
def setupSpec() {}    // run before the first feature method
def cleanupSpec() {}  // run after the last feature method
```
If you don't define a method, Spock will use them implicitly. For example, if you initialized a field in your test class, it will treated as part of the `setup()` method.

### Blocks
It's very common to divide you test into a couple of segments, such as _setup_, _action_ and _assert_. Spock addresses this by something called _blocks_ which is a label for each conceptual phase of a feature method. These are some basic blocks:
```Groovy
given: // Setup test data and mocks
when:  // Peform an action on the class under test
then:  // Evaluate the results
```
and used in a method it looks like this:
```Groovy
def "Should create bank account successfully"() {
  when:
  service.createBankAccount(UUID)

  then:
  notThrown(IllegalArgumentException)
}
```
You could also add descriptions to your blocks and turn your test into pure BDD-style tests
```Groovy
def "Should create bank account successfully"() {
  when: "A bank account is created"
  service.createBankAccount(UUID)

  then: "No error occurs"
  notThrown(IllegalArgumentException)
}
```


### Assertions
Asserting in Spock is dead simple. You just define you expected conditions in the `then:` block like so:

```Groovy
then:
notThrown(IllegalArgumentException)
```
```Groovy
then:
account instanceof BankAccount
account.uuid == UUID
```

### Mocking
Mocking in Spock is nearly as simple. Start by initializing your mock class
```Groovy
 def generatorMock = Mock(IdGenerator)
 ```
 then you could mock a method like this
 ```Groovy
 generatorMock.generate() >> UUID
 ```
 This will make a call to `generate()` return `UUID`.
 
 You could also verify interactions with the mock by adding a cardinality. In this case we expect the `generate()` method to be called twice.
 ```Groovy
 2 * generatorMock.generate()
 ```
 You are also able to create argument constrains (matchers) for the mocks. Let's say that for all values that's a `Type` you want to return the constant `ID`, you can express this as:
 ```Groovy
 generatorMock.generate(_ as Type) >> ID
 ```
 or if you just want it to be a non null value you can express it as:
 ```Groovy
 generatorMock.generate(!null) >> ID
 ```
 You could also iterate of a set of return values such as:
 ```Groovy
 generatorMock.generate() >> [ID1, ID2]
 ```
 This will return `ID1` on the first call and `ID2` on the second call and then `ID1` again on the third.
 Finally, you can combine the things above until one single expression. So
 ```Groovy
 2 * generatorMock.generate(_ as Type) >> [ID1, ID2]
 ```
 means _verify that the `generate()` method is called with an argument that is a `Type` two times and for the first time return `ID1` and for the second time return `ID2`_.

### ...and much more
This was just a glimpse of what's possible with Spock. Please go to the official [Spock doc](https://code.google.com/p/spock) to read more and try out the [sample app](https://github.com/erikryverling/spock-demo/tree/master/src) contained in this repo.
