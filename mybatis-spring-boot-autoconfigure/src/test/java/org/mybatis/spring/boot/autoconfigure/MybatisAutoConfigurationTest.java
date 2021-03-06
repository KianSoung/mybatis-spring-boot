/*
 *    Copyright 2010-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.mybatis.spring.boot.autoconfigure;

import static org.junit.Assert.assertEquals;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.mapper.CityMapper;
import org.mybatis.spring.boot.autoconfigure.repository.CityMapperImpl;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tests for {@link MybatisAutoConfiguration}
 *
 * @author Eddú Meléndez
 */
public class MybatisAutoConfigurationTest {

	private AnnotationConfigApplicationContext context;

	@Test
	public void testNoDataSource() throws Exception {
		this.context = new AnnotationConfigApplicationContext();
		this.context.register(MybatisAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertEquals(0, this.context.getBeanNamesForType(SqlSessionFactory.class).length);
	}

	@Test
	public void testDefaultConfiguration() {
		this.context = new AnnotationConfigApplicationContext();
		this.context.register(EmbeddedDataSourceConfiguration.class,
				MybatisScanMapperConfiguration.class, MybatisAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertEquals(1, this.context.getBeanNamesForType(SqlSessionFactory.class).length);
		assertEquals(1, this.context.getBeanNamesForType(CityMapper.class).length);
	}

	@Test
	public void testWithConfigFile() {
		this.context = new AnnotationConfigApplicationContext();
		EnvironmentTestUtils.addEnvironment(this.context,
				"mybatis.config:mybatis-config.xml");
		this.context.register(EmbeddedDataSourceConfiguration.class,
				MybatisAutoConfiguration.class, MybatisMapperConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertEquals(1, this.context.getBeanNamesForType(SqlSessionFactory.class).length);
		assertEquals(1, this.context.getBeanNamesForType(CityMapperImpl.class).length);
	}

	@Configuration
	@MapperScan("org.mybatis.spring.boot.autoconfigure.mapper")
	static class MybatisScanMapperConfiguration {

	}

	@Configuration
	static class MybatisMapperConfiguration {

		@Bean
		public CityMapperImpl cityMapper() {
			return new CityMapperImpl();
		}

	}

}
