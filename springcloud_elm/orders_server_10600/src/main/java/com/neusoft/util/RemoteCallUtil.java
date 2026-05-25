package com.neusoft.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.neusoft.po.CommonResult;

public class RemoteCallUtil {

	private RemoteCallUtil() {}

	public static <T> T postForObject(RestTemplate restTemplate, String baseUrl, String path, Object body, Class<T> clazz) {
		String url = baseUrl + path;
		return restTemplate.postForObject(url, body, clazz);
	}

	public static <T> CommonResult<T> postForCommonResult(RestTemplate restTemplate, String baseUrl, String path, UriComponentsBuilder builder, Class<CommonResult> rawClazz) {
		String url = builder.toUriString();
		ResponseEntity<CommonResult> resp = restTemplate.postForEntity(url, null, rawClazz);
		return (CommonResult<T>) resp.getBody();
	}
}
