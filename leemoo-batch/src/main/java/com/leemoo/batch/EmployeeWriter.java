package com.leemoo.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Dummy {@link ItemWriter} which only logs data it receives.
 */
@Component("employeeWriter")
@Scope("step")
public class EmployeeWriter implements ItemWriter<Object> {

	private static final Log log = LogFactory.getLog(EmployeeWriter.class);

	private String companyId;

	public String getCompanyId() {
		return companyId;
	}

	@Value("#{jobParameters['companyId']}")
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @see ItemWriter#write(java.util.List)
	 */
	public void write(List<? extends Object> data) throws Exception {
		log.info(data);
		File file = null;
		FileOutputStream fos = null;
		try{
			file = new File("D:/个人学习/证券从业人员/json" + this.getCompanyId() + ".json");
			fos = new FileOutputStream(file);
			for(Object d : data){
				fos.write(String.valueOf(d).getBytes());
			}
		} catch (Exception e){
			log.error(e.getMessage());
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

}
