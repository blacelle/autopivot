/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.av.autopivot.spring;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;

import com.qfs.QfsWebUtils;
import com.qfs.server.cfg.impl.ASpringResourceServerConfig;
import com.qfs.util.impl.QfsArrays;

/**
 * Spring configuration for ActiveUI web application
 *
 * @author ActiveViam
 *
 */
@Configuration
public class ActiveUIResourceServerConfig extends ASpringResourceServerConfig {

	/** The namespace of the ActiveUI web application */
	public static final String NAMESPACE = "ui";

	/** Constructor */
	public ActiveUIResourceServerConfig() {
		super("/" + NAMESPACE);
	}

	@Override
	protected void registerRedirections(final ResourceRegistry registry) {
		super.registerRedirections(registry);
		// Redirect from the root to ActiveUI
		registry.redirectTo(NAMESPACE + "/index.html", "/");
	}

	/**
	 * Registers resources to serve.
	 *
	 * @param registry registry to use
	 */
	@Override
	protected void registerResources(final ResourceRegistry registry) {
		super.registerResources(registry);
		final Set<String> locations = QfsArrays.mutableSet("/", "classpath:META-INF/resources/");
		// ActiveUI web app also serves request to the root, so that the redirection from root to ActiveUI works
		registry.serve(new String[] { QfsWebUtils.url("/") })
				.addResourceLocations(locations.toArray(new String[locations.size()]))
				.setCachePeriod((int) TimeUnit.DAYS.toSeconds(7));
	}

	/**
	 * Gets the extensions of files to serve.
	 * @return all files extensions
	 */
	@Override
	public Set<String> getServedExtensions() {
		return QfsArrays.mutableSet(
				// Default html files
				"html", "js", "css", "map", "json",
				// Image extensions
				"png", "jpg", "gif", "ico",
				// Font extensions
				"eot", "svg", "ttf", "woff", "woff2"
		);
	}

	@Override
	public Set<String> getServedDirectories() {
		return QfsArrays.mutableSet("/");
	}

	@Override
	public Set<String> getResourceLocations() {
		// ActiveUI is integrated using Maven
		// You can read more about this feature here http://support.activeviam.com/documentation/activeui/4.1.0/start/maven-integration/

		// As a result, the resources for ActiveUI web application are found at two locations
		// 1) src/main/resource/activeui: where index.html, favicon.ico are stored
		// 2) classpath:META-INF/resources/activeui/: other scripts necessary for the web application (like app.min.js)
		return QfsArrays.mutableSet("/activeui/", "classpath:META-INF/resources/activeui/");
	}

}
