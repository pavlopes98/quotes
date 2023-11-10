# Quotes Api

## Description 

This simple quotes API project consists of being able to return a specific item (quote) based on an ID and an author from a MongoDB collection. It can also provide the opportunity to return all items in the data collection. 

The collection has a maximum of 50,000 stored items obtained through the QuoteGarden public API (see more here: https://pprathameshmore.github.io/QuoteGarden/#get-quotes).

## Tech Stack

- Kotlin
- Spring
- Gradle
- MongoDB

## How to run

To run the project: 

- Use Java 17
- Gradle JVM must be of Java 17
- Have a MongoDB instance running on localhost:27017

When in the project folder, run `./gradlew bootRun` to run the project. If you're using Windows, run `gradlew.bat bootRun`

If you have Intellij, you can run the project by running the application bootRun task on the Gradle tab.

## Time Spent

- 3 days