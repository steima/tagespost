# Java Client for www.tages-post.at

This is a client library provides Java integration for [tages-post.at](https://www.tages-post.at) which is a fully automated lettershop solution provided by the Austrian [Post AG](https://www.post.at/). The service is capable of printing letters, wrap them into envelopes and mail them.

The service works by providing properly formatted PDF documents and settings meta-data to a bulk shipping service. The service can be used from low volume daily (business) mails to large volume mailings.

## Disclaimer

I (Matthias Steinbauer <matthias@steinbauer.org>) the author of this client library am not affiliated with [tages-post.at](https://www.tages-post.at) or [Post AG](https://www.post.at/). This client library was created as it is used in one of my projects: [DEMANDA Inkassomanagement und -service GmbH](https://demanda.at). As such it is maintained as long as it is necessary for DEMANDA, however I do not provide any warranties or even guarantee that the library works correctly. Please refer to the [license](LICENSE) for details.

If you are intending to use this library you will need access credentials as provided by [tages-post.at](https://www.tages-post.at). There are two steps necessary to use fully automated integration with their service:

 1. Create an account on their website
 2. Contact sales to upgrade your account for `D360-SFTP-IN`

## How to Use

In general I see three stratgies to use this code in your projects:

 1. Use the provided Maven / Gradle dependencies
 2. Download the JAR from releases
 3. Clone this repository and somehow integrate the code into your code

### Maven / Gradle Dependencies

Add the following Maven repository to your project:

```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

With the [jitpack.io](https://jitpack.io) repository in place you can then add the following dependency to your projects `pom.xml`.

```xml
<dependency>
    <groupId>com.github.steima</groupId>
    <artifactId>tagespost</artifactId>
    <version>1.0.0</version>
</dependency>
```

Please refer to [jitpack.io](https://jitpack.io) for other dependency management systems.

### Using the Library

There are three things necessary to successfully upload and process a PDF document with [tages-post.at](https://www.tages-post.at).

 1. Obviously a PDF document (make sure that fonts are integrated in the PDF)
 2. Authentication credentials
 3. Meta-information describing settings for the production process such as whether color or duplex printing are allowed

You can use the various constructors of the class `TagesPostJobSettings` to create print settings. The parameters use self describing `enum` constants. The following example creates a settings object with manual job approval, color print, duplex print, the letter is sent to a national address as a registered letter, and there is no logo to be printed on the envelope.

As a last parameter there is a filename provided. The filenames must end in `.pdf` and your filenames must be unique.

```java
String filename = "2018-03-28-invoice-38.pdf";
TagesPostJobSettings settings = new TagesPostJobSettings(
	Approval.ManualApproval,
	ColorPrint.Color,
	DuplexPrint.Duplex,
	DeliveryType.NationalRegistered,
	EnvelopeLogo.No,
	filename);
```

With such a settings object in place the file can then be sent by calling any of the static `send()` methods of `TagesPostClient`.

```java
TagesPostClient.send(
	"your-user", "/opt/my-application/private.key", settings,
	new FileInputStream("/opt/my-application/invoices/2018-03-28-invoice-38.pdf")
);
```