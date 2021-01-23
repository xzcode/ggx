package com.ggx.docs.core.protobuf.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.ggx.docs.core.model.Doc;
import com.ggx.docs.core.model.Model;
import com.ggx.docs.core.model.ModelProperty;
import com.ggx.docs.core.model.Namespace;
import com.ggx.docs.core.protobuf.ProtoFile;
import com.ggx.docs.core.protobuf.ProtoFileConverter;
import com.ggx.docs.core.protobuf.ProtoMessage;

/**
 * proto文件转换protobufjs的typescript消息类文件 转换器
 *
 * @author zai 2020-06-20 11:39:38
 */
public class ProtoToTypeScriptClassConverter implements ProtoFileConverter {

	private static String IMPORT_LINE = "import { Message, Type, Field, OneOf, MapField } from 'protobufjs/light';\n";

	private static String ENTER_LINE = "\n";

	@Override
	public List<ProtoFile> convertToProtoFileList(Doc doc) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Namespace> namespaces = doc.getNamespaces();

		List<ProtoFile> protoFiles = new ArrayList<>(namespaces.size());

		for (Entry<String, Namespace> entry : namespaces.entrySet()) {
			Namespace namespace = entry.getValue();
			List<Model> models = namespace.getModels();

			ProtoFile protoFile = new ProtoFile(namespace.getName().toLowerCase() + ".ts");

			StringBuilder sb = new StringBuilder(8192);
			String author = null;
			if (doc.getAuth() == null) {
				author = "GGX Docs " + this.getClass().getSimpleName();
			}else {
				author = doc.getAuth();
			}
			sb.append(ENTER_LINE).append("//Author : ").append(author).append(ENTER_LINE)
					.append("//Namespace : " + namespace.getName()).append(ENTER_LINE)
					.append("//Description : " + namespace.getDescription()).append(ENTER_LINE)
					.append("//Create Date : " + dateFormat.format(new Date())).append(ENTER_LINE).append(ENTER_LINE)
					.append(ENTER_LINE).append(IMPORT_LINE).append(ENTER_LINE).append(ENTER_LINE).append(ENTER_LINE);
			protoFile.setContentStart(sb.toString());

			sb.setLength(0);

			protoFile.setContentEnd(sb.toString());

			sb.setLength(0);

			for (Model model : models) {

				String messageName = doc.getMessageModelPrefix() + model.getClazz().getSimpleName();

				String actionId = (StringUtils.isNotEmpty(model.getActionId()) ? doc.getActionIdPrefix() : "")
						+ model.getActionId();
				sb.append("// ").append(model.getDesc()).append(ENTER_LINE).append("// ").append(actionId)
						.append(ENTER_LINE).append("export class ").append(messageName).append(" extends Message<")
						.append(messageName).append("> {\n\n").append("  ").append("static readonly ACTION_ID = '")
						.append(actionId).append("';\n").append(ENTER_LINE);

				int seq = 0;

				List<ModelProperty> properties = model.getProperties();

				for (ModelProperty property : properties) {

					Field field = property.getField();
					seq++;

					// 跳过删除的字段
					Deprecated deprecated = field.getAnnotation(Deprecated.class);
					if (deprecated != null) {
						continue;
					}
					// 跳过忽略的字段
					if (Modifier.isTransient(field.getModifiers())) {
						continue;
					}

					sb.append("  ").append("// ").append(property.getDesc()).append(ENTER_LINE).append("  ")
							.append("@Field.d(").append(seq).append(", '")
							.append(getFieldProtoDataType(field, doc.getMessageModelPrefix())).append("', '")
							.append(getFieldProtoModifier(field)).append("')\n").append("  ").append("public ")
							.append(field.getName()).append(": ").append(getFieldDataType(doc, field))
							.append(" | undefined;\n").append(ENTER_LINE);

				}
				sb.append("}").append(ENTER_LINE).append(ENTER_LINE);

				protoFile.addMessage(new ProtoMessage(messageName, sb.toString()));

				sb.setLength(0);

			}
			protoFiles.add(protoFile);
		}
		Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
		for (ProtoFile pfile : protoFiles) {

			pfile.getMessages().sort((a, b) -> {
				return com.compare(a.getMessageName(), b.getMessageName());
			});
		}
		return protoFiles;
	}

	private String getFieldDataType(Doc doc, Field field) {
		String simpleNamePrefix = doc.getMessageModelPrefix();
		Class<?> type = field.getType();

		if (simpleNamePrefix == null) {
			simpleNamePrefix = "";
		}

		String dataTypeString = getTypescriptBasicDataTypeString(type);
		if (dataTypeString == null) {

			if (type == List.class || type == ArrayList.class || type == LinkedList.class) {

				Type genericType = field.getGenericType();
				if (null == genericType) {
					throw new UnknownError("Unknow Generic Type!!");
				}
				if (genericType instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) genericType;
					// 得到泛型里的class类型对象
					Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
					return simpleNamePrefix + actualTypeArgument.getSimpleName() + "[]";
				}
			}
			return simpleNamePrefix + type.getSimpleName();
		} else {
			return dataTypeString;
		}
	}

	private String getTypescriptBasicDataTypeString(Class<?> type) {

		if (type == String.class || type == String[].class) {
			return "string";
		}
		if (type == boolean.class || type == Boolean.class) {
			return "boolean";
		}
		if (type == int.class || type == Integer.class || type == int[].class || type == Integer[].class) {
			return "number";
		}
		if (type == long.class || type == Long.class || type == long[].class || type == Long[].class) {
			return "number";
		}
		if (type == double.class || type == Double.class || type == double[].class || type == Double[].class) {
			return "number";
		}
		if (type == float.class || type == Float.class || type == float[].class || type == Float[].class) {
			return "number";
		}
		return null;
	}

	@Override
	public List<File> convertToProtoFileAndOutput(Doc doc, String outpath) {
		File path = new File(outpath);
		if (!path.exists()) {
			path.mkdir();
		}
		List<ProtoFile> list = convertToProtoFileList(doc);
		List<File> files = new ArrayList<>(list.size());
		for (ProtoFile protoFile : list) {
			File file = new File(outpath + "/" + protoFile.getFilename());
			try (FileOutputStream os = new FileOutputStream(file);) {
				List<ProtoMessage> messages = protoFile.getMessages();
				StringBuilder sb = new StringBuilder(1024);
				sb.append(protoFile.getContentStart());
				for (ProtoMessage msg : messages) {
					sb.append(msg.getContent());
				}
				sb.append(protoFile.getContentEnd());
				os.write(sb.toString().getBytes(Charset.forName("utf-8")));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			files.add(file);
		}
		return files;
	}

	@Override
	public String convertToProtoString(Doc doc) {
		List<ProtoFile> list = convertToProtoFileList(doc);
		StringBuilder sb = new StringBuilder(2048);
		for (ProtoFile protoFile : list) {
			List<ProtoMessage> messages = protoFile.getMessages();
			sb.append(protoFile.getContentStart());
			for (ProtoMessage msg : messages) {
				sb.append(msg.getContent());
			}
			sb.append(protoFile.getContentEnd());
		}
		return sb.toString();
	}

}
