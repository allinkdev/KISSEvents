[DietrichEvents]: https://github.com/FlorianMichael/DietrichEvents

# KISSEvents

A simple, fast, and configurable event system for Java 8+.

# Usage

To register a Listener (a class that implements the `Listener` interface provided by this library), please simply invoke
`EventSystem.registerListener` on the listener instance.

There is a thread-safe EventSystem instance initialized by default at `EventSystem.SYNCHRONISED`.
There is a non-thread-safe EventSystem (slightly faster for simple, mono-threaded applications) instance initialized by
default at `EventSystem.UNSYNCHRONISED`;

To post an event, simply invoke `EventSystem.post` and pass the event object.

If you want to post the event without blocking the thread you are calling `EventSystem.post` from, you can
invoke `EventSystem.deferPost` instead on any synchronised instance of EventSystem.

By default, this library uses a HashSet to store references to the listeners, and a HashMap to map the event classes to
HashSets of the associated listeners.

If you want to change the type of Map or Collection to something else, you can construct the EventSystem with suppliers
providing new instances of your custom Map/Collection implementation, and whether the EventSystem will be synchronised.
The EventSystem synchronised value should simply represent if the provided Collection implementation provides
synchronisation.

For example, if I wanted to use a LinkedHashSet instead of the default HashSet, I would create an unsynchronised
EventSystem with:

```java
new EventSystem(HashMap::new,LinkedHashSet::new,false);
```

If I wanted this LinkedHashSet EventSystem to be synchronised, I would implement synchronisation by constructing the
EventSystem by:

```java
new EventSystem(()->Collections.synchronizedMap(new HashMap<>()),()->Collections.synchronizedSet(new LinkedHashSet<>()),true);
```

This library was heavily inspired by [DietrichEvents]. If you're looking for a more configurable library, or you want to
do more than listening for a single event (i.e. Pre and Post events) in every Listener interface, you should
consider using it instaed.

# Installation

## Repository

### Maven

```xml

<repository>
    <id>jitpack</id>
    <name>Jitpack</name>
    <url>https://jitpack.io/</url>
</repository>
```

### Gradle Groovy

```groovy
maven {
    name = 'Jitpack'
    url = 'https://jitpack.io/'
}
```

### Gradle Kotlin

```kotlin
maven("https://jitpack.io")
```

## Dependency

### Maven

```xml

<dependency>
    <groupId>com.github.allinkdev</groupId>
    <artifactId>KISSEvents</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle Groovy

```groovy
implementation 'com.github.allinkdev:KISSEvents:1.0.0'
```

### Gradle Kotlin

```kotlin
implementation("com.github.allinkdev:KISSEvents:1.0.0")
```