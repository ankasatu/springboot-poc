# springboot-poc
 
### Running with newrelic
#### Using IDE: IntelliJ IDEA
add `-javaagent:$MODULE_DIR$/lib/newrelic/newrelic.jar` on `VM options`

See this link for more details: https://stackoverflow.com/questions/50938383/how-to-set-jvm-arguments-in-intellij-idea

#### Using Jar File
1\. Build jar file
```shell
$ mvnw clean package
```
2\. Run
```shell
$ java -javaagent:C:/path-to-this-project-dir/lib/newrelic/newrelic.jar -jar target/demo-0.0.1-SNAPSHOT.jar
```