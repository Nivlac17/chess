# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTKuvL5-AFoOxyTAADIQaJJAJpDJZbt5IvFEvVOpNVoGdQJNBD0WjWYvN4cA7FkGFfMlj0b4oB++ivEsALnAWspqig5QIIePKwgeR6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhy3K8gagrCjAwGhmRbqdlh5Q0dojrOgSuEsuUnrekGjIBoJ2ikUyzGRhy0YwLGwYcVhnYAWmKE8tmuaYEpLHXmmw63KOC6Tn006zs246tn0P7aZ22Q9jA-aDr0AzEaMBlfMZjamROFlmJwq5rt4fiBF4KDoPuh6+MwJ7pJkmA2ZeRTUDe0gAKJ7sl9TJc0LSPqoz7dO5c7tn+ZxAiWBXoBpUEdjBTryjACH2BFyHhb6aEYphcrYdxTG8UYKDcJkslCdOIautKkmlNI-UUoYQ3yZ1ilVcpLVgGpCB5ktWmJTpMCWdt1kXmAfYDkOu0+SufmmOugUBJCtp7tCMAAOKjqyUVnrFh1sr+5QVE96VZfYo75UGJloEVbJKeU5Xg5pNXgsgsTIdCbUYZxibdeJvXkmAc3yMJdYeWgYlmhGhSWlRMYifIQphDDjFY+aSZXrB5R48A6NdTIPGkvV0IvaMqiwiT5ETZTfOxDAEAAGbPaODpjUz8N1Q9sQ1NLAtqJzi2lcpKNqOpcM1ZcPRTEDgvjJU-TmygACS0iWwAjL2ADMAAsTynpkBoVhMXw6AgoANj7+l+08NsAHKjn7eyNHtxzM12ORHXZJ29Gbr2WxU1ujvbTuux7Uxe-qzn3H0-uByAwelysXyR9H5d7DAcfnZdV0BZu2A+FA2DcPAuqZHLowpNF57J992mlLeDSA8DwSg0TQ716M8fVf+S3Qwvc5L6OUcr5VusT6z-GZLCcADygmuo1i2s4T1vM4+zBMzkTIsSeTlE8lTcbaLTMD04rCMys4IyWphzBSd9GbuhgCfS+o5YTLxQG-caH8rQ2iHigeMC1E5Q37l6TIa0NqH0Tj9U2fQbZ53KM7d2Z1fwHWTsdByZCKEOyoQXM6y427XU3JYfqCFkgwAAFIQB5BgwIAcg6fXHsbNM1RKT3haDbEGhNt69F7sAXhUA4AQAQlAWYLDV7FVwTDIc6jNHaN0fo3O0hILEJZrVEBAArERaBYTOJ5FfFAaJ2qmAgZjUm+FcZgOfmDZBTNUHi3Zn-ABPMgH2PBKAn+8hfHYP8eGQJmsEHWLCWTdkXIv4YNomEe2DNSZbQceUTWt80l4XotgLQmRMlmMoBY6AOSKKlEpPUvUhTf50RKRA5MG8hEuMIQfVM5STa0KsonOKKd7KnSXL5LhHcgpQA0V2L0sBgDYF7oQeIiQR4fTmUfEov0UppQyllYwEMhm6z4twPAwtxnQXiXVEAjy4QdVghqWJDytnPMAR0xAWz-7Kl6fIWY7FIWJLkskwZ697mbLwGMo29ipmGPobZBZ6clkriAA