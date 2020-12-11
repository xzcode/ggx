package com.ggx.files.service.entity;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(GGXFilesTemporary.COLLECTION_NAME)
public class GGXFilesTemporary {
	
	
	public static final String COLLECTION_NAME = "ggx_files_temporary";
	
	public static final String UID = "_id";
	public static final String CREATE_DATE = "create_date";
	
	@Id
    @Field
	private ObjectId uid;
	
	
	@Indexed(direction = IndexDirection.ASCENDING, background = true)
	@Field
	private Date createDate;


	public ObjectId getUid() {
		return uid;
	}


	public void setUid(ObjectId uid) {
		this.uid = uid;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	

	

}
