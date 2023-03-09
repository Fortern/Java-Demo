package xyz.fortern.util

import org.apache.http.auth.AuthScope
import org.apache.http.auth.Credentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClients
import org.springframework.core.io.Resource
import org.springframework.http.HttpMethod
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.io.InputStream

/**
 * 简单的带有 Digest Auth 的下载工具。可用于Onvif摄像头抓图下载。
 *
 * 使用示例：
 *
```Java
 *  InputStream inputStream = getResponseEntityWithDigestAuth(
 *      "192.168.1.236/onvif-http/snapshot?Profile_1",
 *      HttpMethod.GET,
 *      new UsernamePasswordCredentials("admin", "password")
 *  );
 *  String info = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
```
 *
 * @author Fortern
 */
fun getResponseWithDigest(
	httpUrl: String,
	method: HttpMethod,
	credentials: Credentials,
): InputStream? {
	val uriComponents = UriComponentsBuilder.fromHttpUrl(httpUrl).build()
	val credentialsProvider: CredentialsProvider = BasicCredentialsProvider()
	credentialsProvider.setCredentials(
		AuthScope(uriComponents.host, uriComponents.port),
		credentials
	)
	
	val httpclient: HttpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build()
	val restTemplate = RestTemplate(HttpComponentsClientHttpRequestFactory(httpclient))
	
	val exchange = restTemplate.exchange(uriComponents.toUri(), method, null, Resource::class.java)
	return exchange.body?.inputStream
}
