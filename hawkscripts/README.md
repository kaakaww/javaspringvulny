
## Hawkscripts

A directory of kotlin scripts to customize [HawkScan](https://docs.stackhawk.com/stackhawk-cli/#install-with-zip-file) with 
[IntelliJ IDE](https://www.jetbrains.com/idea/download) support via gradle. 

To enable IDE support...

1. Open a shell in the root of the javaspringvulny repository and run...
    ```shell
    ./gradlew :hawkscripts:download
    ```
   This will download the hawk scripts sdk zip into the `hawkscripts/build` directory as 
   required by the dependencies defined in [hawkscripts.gradle.kts](hawkscripts.gradle.kts). 
1. Start the [IntelliJ IDE](https://www.jetbrains.com/idea/download)
1. Open javaspringvulny as a new gradle project

    ![intellij-new-project-1.png](help-images%2Fintellij-new-project-1.png)
    
    ![intellij-new-project-2.png](help-images%2Fintellij-new-project-2.png)
    
    ![intellij-new-project-3.png](help-images%2Fintellij-new-project-3.png)

1. **Wait for the indexer!**
   ![intellij-new-project-4.png](help-images%2Fintellij-new-project-4.png)

When indexing is complete open any of the `.kts` files in the
defined source directories `authentication, session, httpsender, active, proxy` 
to see activated code highlighting, auto-completion, and inline compilation errors.

Use the [hawk perch]() and [hawk validate auth --watch]() to 
develop and test authentication and session scripts against your 
running web API's. 