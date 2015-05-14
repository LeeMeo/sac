/**   
* Title: EmployeeReader.java 
* Package com.leemoo.batch 
* Description: TODO
* Author LeeMoo
* Date 2015年5月13日 下午4:16:14 
* Version V1.0
* Copyright(C) CITICS 2014-2018, All Rights Reserved
*/ 
package com.leemoo.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** 
 * ClassName: EmployeeReader <br/> 
 * date: 2015年5月13日 下午4:16:14 <br/> 
 * 
 * @author LeeMoo
 * @version  
 * @since JDK 1.7
 */
@Component("employeeReader")
@Scope("step")
public class EmployeeReader implements ItemReader<String> {
	
	private String companyId;
	
	public String getCompanyId() {
		return companyId;
	}
	
	@Value("#{jobParameters['companyId']}")
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String read() throws Exception{
		String url = "http://person.sac.net.cn/pages/registration/train-line-register!search.action";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("filter_EQS_PTI_ID", ""));
		nvps.add(new BasicNameValuePair("filter_EQS_AOI_ID", this.getCompanyId()));
		nvps.add(new BasicNameValuePair("ORDERNAME", "PP#PTI_ID,PP#PPP_NAME"));
		nvps.add(new BasicNameValuePair("ORDER", "ASC"));
		nvps.add(new BasicNameValuePair("sqlkey", "registration"));
		nvps.add(new BasicNameValuePair("sqlval", "SEARCH_FINISH_PUBLICITY"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		
		CloseableHttpResponse response = httpclient.execute(httpPost);
		int code = response.getStatusLine().getStatusCode();
		System.out.println(code);
		HttpEntity entity = null;
		try{
			entity = response.getEntity();
			String ret = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			return ret;
		} finally {
			IOUtils.closeQuietly(response);
		}
	}

	
}
