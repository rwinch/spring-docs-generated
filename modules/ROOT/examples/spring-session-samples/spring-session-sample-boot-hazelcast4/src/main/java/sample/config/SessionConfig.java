/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.config;

import com.hazelcast.config.AttributeConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SerializerConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSession;
import org.springframework.session.hazelcast.Hazelcast4IndexedSessionRepository;
import org.springframework.session.hazelcast.Hazelcast4PrincipalNameExtractor;
import org.springframework.session.hazelcast.HazelcastSessionSerializer;

// tag::class[]
@Configuration
public class SessionConfig {

	@Bean
	public Config hazelcastConfig() {
		Config config = new Config();
		NetworkConfig networkConfig = config.getNetworkConfig();
		networkConfig.setPort(0);
		networkConfig.getJoin().getMulticastConfig().setEnabled(false);
		AttributeConfig attributeConfig = new AttributeConfig()
				.setName(Hazelcast4IndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
				.setExtractorClassName(Hazelcast4PrincipalNameExtractor.class.getName());
		config.getMapConfig(Hazelcast4IndexedSessionRepository.DEFAULT_SESSION_MAP_NAME)
				.addAttributeConfig(attributeConfig).addIndexConfig(
						new IndexConfig(IndexType.HASH, Hazelcast4IndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE));
		SerializerConfig serializerConfig = new SerializerConfig();
		serializerConfig.setImplementation(new HazelcastSessionSerializer()).setTypeClass(MapSession.class);
		config.getSerializationConfig().addSerializerConfig(serializerConfig);
		return config;

	}

}
// end::class[]
