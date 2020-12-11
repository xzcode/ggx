package com.ggx.files.service.config.spring;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ggx.files.service.config.spring.converter.StringToDateConverter;

@Configuration
@EnableWebMvc
public class GGXFilesSpringMvcConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(ObjectId.class, new ToStringSerializer(ObjectId.class));
		simpleModule.addDeserializer(ObjectId.class, new JsonDeserializer<ObjectId>() {
			@Override
			public ObjectId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
					throws IOException, JsonProcessingException {
				return new ObjectId(jsonParser.getValueAsString());
			}
		});
		jackson2HttpMessageConverter.getObjectMapper().registerModule(simpleModule);
		converters.add(jackson2HttpMessageConverter);
	}

	/**
	 * 添加自定义的Converters和Formatters.
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToDateConverter());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
		.addMapping("/**")
        .allowedOrigins("*") //允许跨域的域名，可以用*表示允许任何域名使用
        .allowedMethods("*") //允许任何方法（post、get等）
        .allowedHeaders("*") //允许任何请求头
        ;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		// 适配swagger
		registry.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
	}

}
