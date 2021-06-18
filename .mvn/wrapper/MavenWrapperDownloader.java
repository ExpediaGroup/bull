/*
 * Copyright 2007-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static java.lang.Long.MAX_VALUE;
import static java.lang.System.exit;
import static java.lang.System.getenv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;

/**
 * Maven Wrapper download class.
 */
public final class MavenWrapperDownloader {
    /**
     * Path to the maven-wrapper.properties file, which might contain a downloadUrl property to
     * use instead of the default one.
     */
    private static final String MAVEN_WRAPPER_PROPERTIES_PATH = ".mvn/wrapper/maven-wrapper.properties";

    private static final String WRAPPER_URL_PROPERTY_NAME = "wrapper.url";
    private static final String MAVEN_WRAPPER_JAR_PATH_PROPERTY_NAME = "maven.wrapper.jar.path";
    private static final String ENV_MVNW_USERNAME_PROPERTY_NAME = "mvnw.username.env.var.name";
    private static final String ENV_MVNW_PASSWORD_PROPERTY_NAME = "mvnw.password.env.var.name";

    /**
     * Private constructor.
     */
    private MavenWrapperDownloader() {}

    /**
     * Maven wrapper downloader method.
     * @param args command line arguments
     */
    public static void main(final String... args) {
        System.out.println("- Downloader started");
        var baseDirectory = new File(args[0]);
        System.out.println("- Using base directory: " + baseDirectory.getAbsolutePath());
        try {
            var properties = parsePropertiesFile(MAVEN_WRAPPER_PROPERTIES_PATH, baseDirectory);
            String url = properties.getProperty(WRAPPER_URL_PROPERTY_NAME);
            System.out.println("- Downloading from: " + url);
            var outputFile = new File(baseDirectory.getAbsolutePath(), properties.getProperty(MAVEN_WRAPPER_JAR_PATH_PROPERTY_NAME));
            if (!outputFile.getParentFile().exists()) {
                if (!outputFile.getParentFile().mkdirs()) {
                    System.out.println("- ERROR creating output directory '" + outputFile.getParentFile().getAbsolutePath() + "'");
                }
            }
            System.out.println("- Downloading to: " + outputFile.getAbsolutePath());
            downloadFileFromURL(properties.getProperty(ENV_MVNW_USERNAME_PROPERTY_NAME), properties.getProperty(ENV_MVNW_PASSWORD_PROPERTY_NAME), url, outputFile);
            System.out.println("Done");
            exit(0);
        } catch (final Throwable e) {
            System.out.println("- Error downloading");
            e.printStackTrace();
            exit(1);
        }
    }

    /**
     * Loads a given property file.
     * @param propertiesFileName property file name
     * @param baseDirectory the directory containing the property file
     * @return an instance of {@link Properties}
     * @throws Exception if the parsing fails
     */
    private static Properties parsePropertiesFile(final String propertiesFileName, final File baseDirectory) throws Exception {
        var mavenWrapperPropertyFile = new File(baseDirectory, propertiesFileName);
        if (!mavenWrapperPropertyFile.exists()) {
            throw new Exception("ERROR: missing properties file: " + propertiesFileName);
        }
        var properties = new Properties();
        try (var mavenWrapperPropertyFileInputStream = new FileInputStream(mavenWrapperPropertyFile)) {
            properties.load(mavenWrapperPropertyFileInputStream);
            return properties;
        } catch (IOException e) {
            throw new IOException("- ERROR loading '" + MAVEN_WRAPPER_PROPERTIES_PATH + "'");
        }
    }

    /**
     * Downloads a file from a given url.
     * @param username the environment variable name containing the username required for the authentication
     * @param password the environment variable name containing the password required for the authentication
     * @param urlString the url from which the file has to be downloaded
     * @param destination where the file has to be copied
     * @throws Exception in case the download fails
     */
    private static void downloadFileFromURL(final String username, final String password, final String urlString, final File destination) throws Exception {
        if (getenv(username) != null && getenv(password) != null) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(getenv(username), getenv(password).toCharArray());
                }
            });
        }
        var website = new URL(urlString);
        try (var fos = new FileOutputStream(destination)) {
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            fos.getChannel().transferFrom(rbc, 0, MAX_VALUE);
        }
    }
}
