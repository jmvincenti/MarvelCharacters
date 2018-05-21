# MarvelClient
Android playground app using marvel APIs. See [Marvel API](http://developer.marvel.com)

## how to build app
Update the **environment.gradle** file:
```gradle
ext {
    devEnvironment = [
            baseUrl : "https://gateway.marvel.com/",
            publicKey : "YOUR_PUBLIC_KEY",
            privateKey : "YOUR_PRIVATE_KEY"
    ]

    prodEnvironment = [
            baseUrl : "https://gateway.marvel.com/",
            publicKey : "YOUR_PUBLIC_KEY",
            privateKey : "YOUR_PRIVATE_KEY"
    ]
}
```
