## [Selenium](https://www.selenium.dev)

![Selenium](../images/selenium-banner.png)

Selenium is an umbrella project for a range of tools and libraries that enable and support the automation of web browsers.

It provides extensions to emulate user interaction with browsers, a distribution server for scaling browser allocation, and the infrastructure for implementations of the W3C WebDriver specification that lets you write interchangeable code for all major web browsers.


Follow the [Selenium Documentation for more information](https://www.selenium.dev/documentation/).

### Testing with [javaspringvulny](https://github.com/kaakaww/javaspringvulny)

> Ensure javaspringvulny web application is running on `https://localhost:9000`

> Selenium can be run as part of your gradle task as shown below on the stackhawk.yaml config file.

```yml
  hawk:
  spider:
    maxDurationMinutes: 5
    base: false
    custom:
      command: ./gradlew :test --tests "com.stackhawk.selenium.TestBrowser"
      logOutputToForeground: true
```

### Scanning with HawkScan

To use Selenium custom scan discovery, you will need to specify the http proxy server setting.

> `HTTP_PROXY` is a system environment set by the user.


```kt
fun getBrowser(browserName: String?): WebDriver {
    val driver: WebDriver = if (browserName != null && browserName == "chrome") {
        val options: ChromeOptions = ChromeOptions()
        val proxy: Proxy = Proxy()
        proxy.httpProxy = System.getenv("HTTP_PROXY") // This is required for Selenium scan discovery! 
        options.setProxy(proxy)
        ChromeDriverManager.getInstance().setup()
        ChromeDriver(options)
    } else {
        val options: FirefoxOptions = FirefoxOptions()
        val proxy: Proxy = Proxy()
        proxy.httpProxy = System.getenv("HTTP_PROXY") // This is required for Selenium scan discovery! 
        options.setHeadless(true)
        options.setProxy(proxy)
        FirefoxDriverManager.getInstance().setup()
        FirefoxDriver(options)
    }
    return driver
}
```

You can use the sample [selenium-stackhawk.yml](https://github.com/stackhawk/stackhawk-custom-image/blob/main/integrations/selenium/selenium-stackhawk.yml) file for an example of scanning a web application with it.

### Selenium Best Practices

No one approach works for all situations.

Please refer to the selenium [Test Pratices](https://www.selenium.dev/documentation/test_practices/) for more information
