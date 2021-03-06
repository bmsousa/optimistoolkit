/**
 *  Copyright 2013 University of Leeds
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.optimis.vc.api;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import eu.optimis.types.xmlbeans.servicemanifest.XmlBeanServiceManifestDocument;
import eu.optimis.vc.api.Core.ProgressException;
import eu.optimis.vc.api.DataModel.GlobalConfiguration;
import eu.optimis.vc.api.DataModel.ProgressData;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import eu.optimis.manifest.api.sp.Manifest;

/**
 * Unit test for VmcApi.
 * 
 * @author Django Armstrong (ULeeds)
 * @version 0.0.6
 */
public class VmcApiTest extends TestCase {
	protected final static Logger log = Logger.getLogger(VmcApiTest.class);

	private static String configFilePath = null;
	private static Manifest testManifest;
	private static String imageFormat = null;

	/**
	 * Create the test case.
	 * 
	 * @param testName
	 *            Name of the test case.
	 */
	public VmcApiTest(String testName) {
		super(testName);
	}

	/**
	 * Get current method name
	 * 
	 * @return Method Name
	 */
	public String getCurrentMethodName() {
		StackTraceElement stackTraceElements[] = (new Throwable())
				.getStackTrace();
		return stackTraceElements[1].toString();
	}

	/**
	 * Tests the progress object callback function when we are 100% sure the
	 * state data has been initialised.
	 */
	public void testContextualizeServiceWorkflow() {
		log.info("### TEST: " + getCurrentMethodName() + " STARTED ###");

		ProgressData progressData = null;

		try {
			// Decide which manifest we are going to test with
			Manifest manifest;
			if (testManifest != null) {
				manifest = testManifest;
			} else {
				String serviceId = "3";
				manifest = Manifest.Factory.newInstance(serviceId, "jboss");
			}

			log.info("TEST: Service manifest is:\n" + manifest.toString());

			// Initialise the VMC's configuration providing the path of the
			// config.properties file...
			GlobalConfiguration globalConfiguration;
			if (configFilePath != null) {
				globalConfiguration = new GlobalConfiguration(configFilePath);
			} else {
				globalConfiguration = new GlobalConfiguration();
			}
			VmcApi vmcApi = new VmcApi(globalConfiguration);

			if (imageFormat == null) {
				vmcApi.contextualizeService(manifest);
			} else {
				vmcApi.contextualizeService(manifest, imageFormat);
			}

			// Wait until the service has been registered with the VMC before
			// polling the progress data...
			while (true) {
				try {
					log.info("TEST: Trying to fetch progress data...");
					vmcApi.contextualizeServiceCallback(manifest
							.getManifestId());
					log.info("TEST: No ProgressException...");
					break;
				} catch (ProgressException e) {
					log.warn("TEST: Caught ProgressException due to: "
							+ e.getMessage());
					Thread.sleep(250);
				}
			}

			// Poll the progress data until the completion...
			while (true) {

				// We have progress data, do something with it...
				progressData = vmcApi.contextualizeServiceCallback(manifest
						.getManifestId());

				// We have an error so stop everything!
				if (progressData.isError()) {
					// Say what the error is...
					log.error(progressData.getException().getMessage(),
							progressData.getException());
					assertFalse(progressData.isError()); // For test case to
															// report failure
															// correctly
					return;
				} else {
					log.info("TEST: contextualizeServiceCallbackObject total progress is: "
							+ progressData.getTotalProgress());
					log.info("TEST: contextualizeServiceCallbackObject phase history of Phase with ID: "
							+ progressData.getPhaseName(progressData
									.getCurrentPhaseId())
							+ ", % is: "
							+ progressData.getHistory().get(
									progressData.getCurrentPhaseId()));
				}

				// 250ms delay between polling...
				Thread.sleep(250);

				// Test to see if contextualization has finished...
				if (vmcApi.contextualizeServiceCallback(
						manifest.getManifestId()).isComplete()) {
					log.warn("TEST: Detected contextualization has completed!");
					break;
				}
			}

			// Test that the new manifest has been set...
			assertTrue(progressData.getManifest() != null);
			log.info("TEST: contextualizeServiceCallbackObject altered manifest is: \n"
					+ progressData.getManifest().toString());

			// Test that the history is saved correctly in the progress data.
			for (int i = 0; i < progressData.getHistory().size(); i++) {
				log.info("TEST: contextualizeServiceCallbackObject history of Phase with ID: "
						+ progressData.getPhaseName(i)
						+ ", % is: "
						+ progressData.getHistory().get(i));
			}

		} catch (Exception e) {
			log.error("TEST: Test Failed! Cause: " + e.getCause(), e);
		}

		log.warn("### TEST: " + getCurrentMethodName() + " COMPLETE ###");
	}

	/**
	 * Runs the test suite using the textual runner.
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			log.info("ERROR: No arguements specified!");
		
			log.info("Usage: java -jar VmContextualizer.jar <function> <working_directory> [<options>] ...");
			log.info("  <function> values:");
			log.info("    \"1\": Unit tests");
			log.info("    \"2\": Perf evalauation");
			log.info("    \"3\": Recontextualization Standalone");
			log.info("  <working_directory>:");
			log.info("    Path to config file and working directory");
			log.info("    Value of \"default\" uses hardcoded default config data for unit testing");
			log.info("  Arguments for function \"1\":");
			log.info("    [<manifest_path>]:");
			log.info("      Path to manifest file, includes file name");
			log.info("    [<desired_image_format>]:");
			log.info("      Image format to convert to");
			log.info("  Arguments for function \"3\":");
			log.info("    [<hypervisor_uri>]:");
			log.info("      Hypervisor URI used by recontextualization");
			log.info("    [<recontext_domain>]:");
			log.info("      VM name used by recontextualization");
			
			log.info("Example: java -jar VmContextualizer.jar 1 default ...");
			
		} else {
			if (Integer.parseInt(args[0]) == 1) { // Unit Tests (Option 1)
				
				if (args.length == 1) {
					log.warn("WARNING: No working directory specified using default config, location and manifest!");
				} else {

					if (!args[1].equals("default")) {
						System.out.println(args[1]);
						configFilePath = new String(args[1]);
					}

					if (args.length == 2) {
						log.warn("WARNING: No manifest file specified using default!");
					}
					
					if (args.length == 3) {
						log.info("Using manifest location: " + args[2]);
						File smFile = new File(args[2]);
						XmlBeanServiceManifestDocument doc = null;
						try {
							doc = XmlBeanServiceManifestDocument.Factory
									.parse(smFile);
						} catch (XmlException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						testManifest = Manifest.Factory.newInstance(doc);
					}

					if (args.length == 4) {
						log.info("Using image format: " + args[3]);
						imageFormat = args[3];
					}
				}
				
				TestSuite testSuite = new TestSuite();
				testSuite.addTestSuite(VmcApiTest.class);
				if (configFilePath != null) {
					System.setProperty("configFilePath", configFilePath); //For other TestSuites
				}
				testSuite.addTestSuite(VmcApiRecontextTest.class);
				junit.textui.TestRunner.run(testSuite);
				
			} else if (Integer.parseInt(args[0]) == 2) { // Performance Evaluation (Option 2) 
				
				System.out.println("Running Evaluation:");
				// TODO: Add new evaluation
				System.out.println("Performance evaluation is not implemented");
				
			} else if (Integer.parseInt(args[0]) == 3) { // Recontextualization (Option 3)
				System.out.println("Running the VMC in Recontextualization Mode...");
				VmcApiRecontextTest vmcApiRecontextTest = new VmcApiRecontextTest("");
				if (args.length == 5) {
					
					log.info("Using config file path: " + args[1]); 
					if (!args[1].equals("default")) {
						System.out.println(args[1]);
						configFilePath = new String(args[1]);
					}
					
					log.info("Using working directory: " + args[2]); 
					log.info("Using Hypervisor URI: " + args[3]); //TODO at some point put/get this in the properties file
					log.info("Using VM Name: " + args[4]); //TODO at some point put/get this in the properties file
					
					vmcApiRecontextTest.recontextualize(configFilePath, true, args[3], args[4]);
				} else {
					log.error("ERROR: Not enough arguments for Recontextualization Mode!");
                    log.info("USAGE: java -jar VmContextualizer.jar 3 default <hypervisor_uri> <recontext_domain>");
				}
				
			} else {
				log.error("ERROR: Unknown test number specified!");
			}
		}
	}
}
