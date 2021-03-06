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
package eu.optimis.vc.api.IsoCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import eu.optimis.manifest.api.sp.Manifest;
import eu.optimis.vc.api.Core.SystemCall;
import eu.optimis.vc.api.Core.SystemCallException;
import eu.optimis.vc.api.DataModel.ContextData;
import eu.optimis.vc.api.DataModel.GlobalConfiguration;
import eu.optimis.vc.api.DataModel.VirtualMachine;
import eu.optimis.vc.api.DataModel.ContextDataTypes.EndPoint;
import eu.optimis.vc.api.DataModel.ContextDataTypes.LicenseToken;
import eu.optimis.vc.api.DataModel.ContextDataTypes.SecurityKey;
import eu.optimis.vc.api.DataModel.ContextDataTypes.SoftwareDependency;
import eu.optimis.vc.api.DataModel.Image.Iso;

/**
 * Class to create ISO images for containing contextualization data.
 * 
 * @author Django Armstrong (ULeeds)
 * @version 0.0.4
 */
public class IsoImageCreation {

	private static final String IO_EXCEPTION = "IOException: ";

	private static final String FILE_NOT_FOUND_EXCEPTION = "FileNotFoundException: ";

	private static final String CREATED_FILE = "Created file: ";

	private static final String ATTEMPTING_TO_CREATE_FILE = "Attempting to create file: ";

	protected static final Logger LOGGER = Logger
			.getLogger(IsoImageCreation.class);

	private Iso iso;
	private GlobalConfiguration configuration;
	private SystemCall systemCall;
	private String isoDataDirectory;
	private Boolean addRecontextFiles;

	private Manifest manifest;
	
	private ContextData contextData;
	private VirtualMachine virtualMachine;

	/**
	 * Initialises an instance of the ISO Image creator.
	 * 
	 * @param iso
	 *            The ISO to create.
	 * @param configuration
	 *            Configuration details used when creating the ISO.
	 */
	public IsoImageCreation(Iso iso, GlobalConfiguration configuration,
			Manifest manifest) {
		this.iso = iso;
		this.configuration = configuration;
		this.manifest = manifest;
		this.systemCall = new SystemCall(configuration.getInstallDirectory());
		this.isoDataDirectory = configuration.getContextDataDirectory()
				+ File.separator + iso.getFileName();
		this.addRecontextFiles = configuration.getAddRecontextFiles();
		LOGGER.debug("Read addRecontextFiles from config: " + this.addRecontextFiles);
	}

	/**
	 * Create new IsoImageCreation object for recontextualization
	 * @param configuration The global configuration
	 * @param existingIsoPath The path to the existing ISO file
	 * @throws IOException If the existing ISO cannot be found, or if a temp directory cannot be created.
	 */
	public IsoImageCreation(GlobalConfiguration configuration, String existingIsoPath) throws IOException {	
		String fileName = "recontext_" + existingIsoPath.substring(existingIsoPath.lastIndexOf(File.separator) + 1, existingIsoPath.length());
		// Not used in this context, for VirtualMachine 
		String imageId = "0";
		String uri = configuration.getRepository() + File.separator + fileName;
		String format = "ISO9660";
		// TODO: Not currently used? Investigate.
		String mountPoint =  "/media/context/";
		this.iso = new Iso(imageId, fileName, uri, format, mountPoint);
		this.configuration = configuration;
		this.systemCall = new SystemCall(configuration.getInstallDirectory());
		this.isoDataDirectory = configuration.getContextDataDirectory()
				+ File.separator + iso.getFileName();
		this.addRecontextFiles = configuration.getAddRecontextFiles();
	}
	
	// TODO Add javadoc here
	// 2) Write out the end points to each to there own file in a
	// sub-directory:	
	private void storeEndpoints() {

		File endPointDirectory = new File(isoDataDirectory + File.separator
				+ "endpoints");
		endPointDirectory.mkdirs();

		if (virtualMachine.getEndPoints().size() != 0) {
			for (EndPoint endPoint : virtualMachine.getEndPoints().values()) {
				String name = endPoint.getName();
				String uri = endPoint.getUri();

				// Create the end point with the given name
				File endPointFile = new File(endPointDirectory + File.separator
						+ name + ".properties");
				try {
					LOGGER.debug(ATTEMPTING_TO_CREATE_FILE
							+ endPointFile.getPath());
					endPointFile.createNewFile();
					LOGGER.debug(CREATED_FILE + endPointFile.getPath());
				} catch (IOException e) {
					LOGGER.error("Failed to create endpoint file with name: "
							+ name + ".properties", e);
				}

				Properties props = new Properties();
				props.setProperty(name, uri);
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(
							endPointFile);
					props.store(fileOutputStream,
							"VMC properties file for service endpoints:");
					fileOutputStream.close();
					LOGGER.debug("Writing endpoint complete!");

				} catch (FileNotFoundException e) {
					LOGGER.error(FILE_NOT_FOUND_EXCEPTION + e);
				} catch (IOException e) {
					LOGGER.error(IO_EXCEPTION + e);
				}

			}
		} else {
			LOGGER.warn("No end points to write!");
		}
	}
	
	// TODO Add javadoc here
	// 1) Store the security keys if the are to be added to this VM
	// instance
	private void storeSecurityKeys() {
		File securityKeysDirectory = new File(isoDataDirectory + File.separator
				+ "securitykeys");
		securityKeysDirectory.mkdirs();

		if (virtualMachine.isHasVPNKey() || virtualMachine.isHasSSHKey()
				|| virtualMachine.isHasBTKey() || virtualMachine.isHasDMKey()) {
			if (contextData.getSecurityKeys().size() != 0) {
				for (SecurityKey securityKey : contextData.getSecurityKeys()
						.values()) {
					String name = securityKey.getName();
					byte[] keyData = securityKey.getKeyData();

					File securityKeyFile = null;
					// TODO: change to switch... meh
					if (name.equals("SSH")) {
						securityKeyFile = new File(securityKeysDirectory
								+ File.separator + "SSH.key");
					} else if (name.equals("VPN")) {
						securityKeyFile = new File(securityKeysDirectory
								+ File.separator + "VPN.key");
					} else if (name.equals("BT")) {
						securityKeyFile = new File(securityKeysDirectory
								+ File.separator + "BT.key");
					} else if (name.equals("DM")) {
						securityKeyFile = new File(securityKeysDirectory
								+ File.separator + "DM.key");
					} else {
						LOGGER.warn("Unknown key name type!");
					}

					try {
						LOGGER.debug(ATTEMPTING_TO_CREATE_FILE
								+ securityKeyFile.getPath());
						securityKeyFile.createNewFile();
						LOGGER.debug(CREATED_FILE + securityKeyFile.getPath());
					} catch (IOException e) {
						LOGGER.error(
								"Failed to create security key file with name: "
										+ name + ".key", e);
					}

					// Write out security key...
					LOGGER.warn("Writing out security key with path: "
							+ securityKeyFile.getPath());
					try {
						FileOutputStream fos = new FileOutputStream(
								securityKeyFile);
						fos.write(keyData);
						fos.close();
						LOGGER.debug("Writing security key complete!");

					} catch (FileNotFoundException e) {
						LOGGER.error(FILE_NOT_FOUND_EXCEPTION + e);
					} catch (IOException e) {
						LOGGER.error(IO_EXCEPTION + e);
					}
				}
			} else {
				LOGGER.warn("No security keys to write!");
			}
		}
	}
	
	// TODO Add javadoc here
	// 3) Provide per VM instance configuration information for software
	// dependencies
	private void storeSoftwareDependencies() {
		File softwareDependenciesDirectory = new File(isoDataDirectory
				+ File.separator + "softwaredeps");
		softwareDependenciesDirectory.mkdirs();

		if (virtualMachine.getSoftwareDependencies().size() != 0) {
			for (SoftwareDependency softwareDependency : virtualMachine
					.getSoftwareDependencies().values()) {
				String name = softwareDependency.getArtifactId() + "_"
						+ softwareDependency.getGroupId() + "_"
						+ softwareDependency.getVersion() + ".dep";

				// Create the software dependency files here...
				File softwareDependencyFile = new File(
						softwareDependenciesDirectory + File.separator + name);
				try {
					LOGGER.debug(ATTEMPTING_TO_CREATE_FILE
							+ softwareDependencyFile.getPath());
					softwareDependencyFile.createNewFile();
					LOGGER.debug(CREATED_FILE
							+ softwareDependencyFile.getPath());
				} catch (IOException e) {
					LOGGER.error(
							"Failed to create softwareDependency config file with name: "
									+ name, e);
				}

				// TODO Write out some configuration data to the new file.
				LOGGER.warn("Writing out software dependency configuration data not implemented yet, no support in service manifest!");
			}
		} else {
			LOGGER.warn("No software dependency configuration files to write!");
		}
	}

	// TODO Add javadoc here	
	// 4) Store the license tokens
	private void storeLicenseKeys() {

		File licenseTokenDirectory = new File(isoDataDirectory + File.separator
				+ "licensetoken");
		licenseTokenDirectory.mkdirs();

		if (virtualMachine.getLicenseTokens().size() != 0) {
			for (LicenseToken licenseToken : virtualMachine.getLicenseTokens()
					.values()) {
				String name = "license.token." + licenseToken.getId();

				// Create the end point with the given name and write the
				File licenseTokenFile = new File(licenseTokenDirectory
						+ File.separator + name);
				try {
					LOGGER.debug(ATTEMPTING_TO_CREATE_FILE
							+ licenseTokenFile.getPath());
					licenseTokenFile.createNewFile();
					LOGGER.debug(CREATED_FILE + licenseTokenFile.getPath());
				} catch (IOException e) {
					LOGGER.error("Failed to create licenseToken file with name: "
							+ name, e);
				}

				// Write out license tokens...
				LOGGER.warn("Writing out license token...");
				try {
					FileOutputStream fos = new FileOutputStream(
							licenseTokenFile);
					fos.write(licenseToken.getToken());
					fos.close();
					LOGGER.warn("Writing license token complete!");
				} catch (FileNotFoundException e) {
					LOGGER.error(FILE_NOT_FOUND_EXCEPTION + e);
				} catch (IOException e) {
					LOGGER.error(IO_EXCEPTION + e);
				}
			}
		}
	}
	
	// TODO Add javadoc here
	// 5) Store the entire manifest
	private void storeManifest() {
		File serviceManifestFile = new File(isoDataDirectory + File.separator
				+ "manifest.xml");
		try {
			LOGGER.debug(ATTEMPTING_TO_CREATE_FILE
					+ serviceManifestFile.getPath());
			serviceManifestFile.createNewFile();
			LOGGER.debug(CREATED_FILE + serviceManifestFile.getPath());

			// Write out the manifest file
			FileOutputStream fos = new FileOutputStream(
					serviceManifestFile.getPath());
			fos.write(manifest.toString().getBytes());
			fos.close();
			LOGGER.debug("Writing service manifest complete!");

		} catch (IOException e) {
			LOGGER.error("Failed to create service manfiest file with name: "
					+ serviceManifestFile.getName(), e);
		}
	}
	
	// TODO Add javadoc here
	// 6) Bootstrap script
	private void storeBootstrapScripts(File scriptsDirectory) {
		File bootStrapFile = new File(scriptsDirectory + File.separator
				+ "bootstrap.sh");
		try {
			LOGGER.debug(ATTEMPTING_TO_CREATE_FILE + bootStrapFile.getPath());
			bootStrapFile.createNewFile();
			LOGGER.debug(CREATED_FILE + bootStrapFile.getPath());

			// TODO: This should be stored somewhere else and not hardcoded
			// Mount location is currently hard coded in the init.d scripts of
			// the base VM /mnt/context/
			String bootStrapScript = "#!/bin/bash\n"
					+ "if [ -f /mnt/context/scripts/bootstrap.sh ]; then\n"
					+ "  #Get the public SSH key:\n"
					+ "  PUBLICKEY=`cat /mnt/context/securitykeys/SSH.key | grep ssh-rsa`\n"
					+ "  echo ${PUBLICKEY/user@hostname/root@`hostname`} > /root/.ssh/authorized_keys\n"
					+ "  chmod 700 /root/.ssh\n"
					+ "  chmod 600 /root/.ssh/authorized_keys\n"
					+ "  #Get the private SSH key:\n"
					+ "  cat /mnt/context/securitykeys/SSH.key | head -27 > /root/.ssh/id_rsa\n"
					+ "  chmod 700 /root/.ssh/id_rsa\n"
					+ "  #Run agent script if present\n"
					+ "  if [ -f /mnt/context/scripts/agents.sh ]; then\n"
					+ "    sh /mnt/context/scripts/agents.sh\n" + "  fi\n"
					+ "fi\n";

			// Write out the boostrap file
			FileOutputStream fos = new FileOutputStream(bootStrapFile.getPath());
			fos.write(bootStrapScript.getBytes());
			fos.close();
			LOGGER.debug("Writing boobstrap script complete!");

		} catch (IOException e) {
			LOGGER.error("Failed to create boobstrap script file with name: "
					+ bootStrapFile.getName(), e);
		}
	}
	
	// TODO Add javadoc here
	// 7) Security Agents
	private void storeAgents(File scriptsDirectory) {
		if (virtualMachine.isHasIPS() || virtualMachine.isHasBTKey()
				|| virtualMachine.isHasVPNKey()) {
			LOGGER.debug("Adding agents to ISO");

			// Add the agent tar ball
			String agentsTarBallName = "vpn.tar.gz";
			// Agents tar ball source
			File agentsTarBallFileSource = new File(
					configuration.getAgentsDirectory() + File.separator
							+ agentsTarBallName);
			LOGGER.debug("agentsTarBallFileSource is: "
					+ agentsTarBallFileSource.getPath());
			// Destination folder
			File agentsIsoDirectory = new File(isoDataDirectory
					+ File.separator + "agents");
			agentsIsoDirectory.mkdirs();
			LOGGER.debug("agentsIsoDirectory is: " + agentsIsoDirectory.getPath());
			// Agents tar ball destination
			File agentsTarBallFileDestination = new File(agentsIsoDirectory
					+ File.separator + agentsTarBallName);
			LOGGER.debug("agentsTarBallFileDestination is: "
					+ agentsTarBallFileDestination.getPath());

			// Copy the file to the iso directory
			LOGGER.debug("Copying agent file to ISO directory...");
			FileChannel source = null;
			FileChannel destination = null;
			try {
				if (!agentsTarBallFileDestination.exists()) {
					agentsTarBallFileDestination.createNewFile();
				}
				source = new FileInputStream(agentsTarBallFileSource)
						.getChannel();
				destination = new FileOutputStream(agentsTarBallFileDestination)
						.getChannel();
				destination.transferFrom(source, 0, source.size());
				LOGGER.debug("Copied agent file to ISO directory, size is : "
						+ agentsTarBallFileDestination.length());
				agentsTarBallFileDestination.getTotalSpace();
			} catch (IOException e) {
				LOGGER.error("Failed to create agents tar ball with path: "
						+ agentsTarBallFileDestination.getPath(), e);
			} finally {
				if (source != null) {
					try {
						source.close();
					} catch (IOException e) {
						LOGGER.error(
								"Failed to close source agents tar ball file with path: "
										+ agentsTarBallFileSource.getPath(), e);
					}
				}
				if (destination != null) {
					try {
						destination.close();
					} catch (IOException e) {
						LOGGER.error(
								"Failed to close destination agents tar ball file with path: "
										+ agentsTarBallFileDestination.getPath(), e);
					}
				}
			}

			// Add the agent script
			File agentsFile = new File(scriptsDirectory + File.separator
					+ "agents.sh");
			try {
				LOGGER.debug(ATTEMPTING_TO_CREATE_FILE + agentsFile.getPath());
				agentsFile.createNewFile();
				LOGGER.debug(CREATED_FILE + agentsFile.getPath());

				// TODO: This should be stored somewhere else and not hardcoded
				// Mount location is currently hard coded in the init.d scripts
				// of the base VM /mnt/context/
				String agentsScript = "#!/bin/bash\n"
						+ "#Setup environment\n"
						+ "touch /var/lock/subsys/local\n"
						+ "source /etc/profile\n"
						+ "\n"
						+ "#Extract the agent from the ISO agent directory to /opt/optimis/vpn/\n"
						+ "mkdir -p /opt/optimis\n"
						+ "tar zxvf /mnt/context/agents/" + agentsTarBallName
						+ " -C /opt/optimis/\n"
						+ "chmod -R 777 /opt/optimis/vpn\n" + "\n"
						+ "#Install and start the agents\n" + "\n";

				// Add VPN install and init script to
				// /mnt/context/scripts/agents.sh
				if (virtualMachine.isHasIPS()) {
					agentsScript += "#IPS\n" + "/opt/optimis/vpn/IPS_Meta.sh\n"
							+ "/bin/date > /opt/optimis/vpn/dsa.log\n" + "\n";
				}

				// Add VPN install and init script to
				// /mnt/context/scripts/agents.sh ?
				
				// KMS
				if (virtualMachine.isHasBTKey()) {
					agentsScript += "#KMS\n" + "/opt/optimis/vpn/KMS_Meta.sh\n"
							+ "/bin/date >> /opt/optimis/vpn/scagent.log\n"
							+ "\n";
				}

				// Add VPN install and init script to
				// /mnt/context/scripts/agents.sh
				if (virtualMachine.isHasVPNKey()) {
					agentsScript += "#VPN\n" + "/opt/optimis/vpn/VPN_Meta.sh\n";
				}

				// Write out the agents file
				FileOutputStream fos = new FileOutputStream(
						agentsFile.getPath());
				fos.write(agentsScript.getBytes());
				fos.close();
				LOGGER.debug("Writing agents script complete!");

			} catch (IOException e) {
				LOGGER.error("Failed to create agents script file with path: "
						+ agentsFile.getPath(), e);
			}
		} else {
			LOGGER.debug("Agents not not needed by service!");
		}

	}
	
	// TODO Add javadoc here
	// 8) Add recontextualization files
	private void refactorFiles(boolean addRecontextScripts) {
		LOGGER.debug("addRecontextFiles : " + addRecontextFiles);
		//FIXME changes this to the an ISO structure version number
		if (addRecontextFiles) {
			LOGGER.debug("Performing ISO recontextualization changes...");

			LOGGER.debug("Restructuring ISO");
			// Create the data directory
			String recontextIsoDataDirectory = isoDataDirectory
					+ File.separator + "data";
			// Move the contents in the root of the iso to the new data
			// directory
			LOGGER.debug("Copying context data from: '" + isoDataDirectory + "' to: '" + recontextIsoDataDirectory + "'" );
			
				
				File recontextIsoDataDirectoryFile  = new File(recontextIsoDataDirectory);
				recontextIsoDataDirectoryFile.mkdir();
				
				File[] fileList = new File(isoDataDirectory).listFiles();
				for (File file: fileList) {
					try {	
					if (file.isDirectory()) {
						LOGGER.debug("Considering directory: " + file.getName());
						if (  ! file.getName().equalsIgnoreCase("data") ) {
							File newDir = new File(recontextIsoDataDirectoryFile + File.separator + file.getName());
							FileUtils.copyDirectory(file, newDir);
							LOGGER.debug("Copied directory: " + file.getAbsolutePath() + " to " + newDir.getAbsolutePath());
						}
					} else {
						FileUtils.copyFile(file, new File(recontextIsoDataDirectoryFile + File.separator + file.getName()));
						LOGGER.debug("Copying file: " + file.getName());
					}
					
					} catch (IOException e) {
						LOGGER.error(
								"Failed to copy old context data to recontext data directory",
								e);
					}
				}
			
			//Delete old context data
			LOGGER.debug("Deleting old context data");		
			fileList = new File(isoDataDirectory).listFiles();
			for (File file: fileList) {
				 if ( ! file.getName().equalsIgnoreCase("data") ) {
				 	try {
				 		FileUtils.forceDelete(file);
						LOGGER.debug( "Deleted file or directory: '" + file.getPath() + "'");
					} catch (IOException e) {
						LOGGER.error( "Failed to delete file or directory: '" + file.getPath() + "'" , e);
					}
				} else {
					LOGGER.debug("Skipping deletion of 'data' directory");
				}
			}

			// Add the recontext-scripts directory to the iso
			if (addRecontextScripts) {
				String recontextIsoScriptDirectory = isoDataDirectory
						+ File.separator + "recontext-scripts";
				String recontextScripts = configuration.getInstallDirectory()
						+ File.separator + "templates" + File.separator + "scripts"
						+ File.separator + "recontext";
				LOGGER.debug("Copying recontext files from: '" + recontextScripts + "' to: '" + recontextIsoScriptDirectory + "'" );
				try {
					FileUtils.copyDirectory(new File(recontextScripts), new File(
							recontextIsoScriptDirectory));
				} catch (IOException e) {
					LOGGER.error(
							"Failed to copy old context data to recontext data directory",
							e);
				}
			}

			LOGGER.debug("ISO recontextualization changes completed");
			
		} else {
			LOGGER.debug("Recontext files have not been specified in the VMC config, not altering ISO structure!");
		}
	}
	
	// TODO Add javadoc here
	// 9 ) Add ISO version info to props file 
	private void storeMetaDataFile(String version, String type) {
		LOGGER.debug("Creating meta.data properties file for folder stucture version=" + version);
		Properties metaDataVersion = new Properties();
		metaDataVersion.setProperty("version", version);
		metaDataVersion.setProperty("type", type);
		File metaDataFile = new File(isoDataDirectory + File.separator + ".metadata");
		
		LOGGER.debug("Adding meta.data to ISO");
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(
					metaDataFile);
			metaDataVersion.store(fileOutputStream,
					"VMC ISO data structure version:");
			fileOutputStream.close();
			LOGGER.debug("Writing endpoint complete!");
		} catch (FileNotFoundException e) {
			LOGGER.error(FILE_NOT_FOUND_EXCEPTION + e);
		} catch (IOException e) {
			LOGGER.error(IO_EXCEPTION + e);
		}
	}
	
	/**
	 * Store context data to the local repository. The following context data is
	 * stored: 1) Security Keys 2) End Points 3) Software Dependencies 4)
	 * License Tokens 5) Entire SP manifest 6) Bootstrap script 7) Security
	 * Agents
	 * 
	 * TODO: Refactor reducing duplication of code used for directory and file
	 * operations...
	 * 
	 * @param contextdata
	 *            Used to access the security keys applied at the service level.
	 * @param virtualMachine
	 *            The virtual machine to store context data for.
	 */
	public void storeContextData(ContextData contextData,
			VirtualMachine virtualMachine) {
		
		this.contextData = contextData;
		this.virtualMachine = virtualMachine;
		
		LOGGER.debug("Iso Data Directory is: " + isoDataDirectory + File.separator);

		// Create the directory
		new File(isoDataDirectory).mkdirs();

		// TODO: Add comments to phases of storage
		storeSecurityKeys();

		storeEndpoints();

		storeSoftwareDependencies();

		storeLicenseKeys();
		
		storeManifest();

		//Create scripts directory for bootstrap and agents
		File scriptsDirectory = new File(isoDataDirectory + File.separator
				+ "scripts");
		scriptsDirectory.mkdirs();
		
		storeBootstrapScripts(scriptsDirectory);

		storeAgents(scriptsDirectory);
		
		//Refactor the ISO for recontext support
		refactorFiles(true);
		
		storeMetaDataFile("2", "context");
	}

	// TODO Add javadoc here
	// Creates a new recontextualization image (with test data), eventually will
	// take as input recontextualization data specific to an IP
	public void storeRecontextData(ContextData contextdata,
			VirtualMachine virtualMachine) {
		
		//TODO: contextData used for security keys but not implemented in this function yet 
		this.contextData = contextdata;
		this.virtualMachine = virtualMachine;
		
		LOGGER.debug("Iso Data Directory is: " + isoDataDirectory + File.separator);
		
		// Create the directory
		new File(isoDataDirectory).mkdirs();
		
		// Add endpoints to be stored in the recontext ISO
		storeEndpoints();

		//Refactor the ISO for recontext support without adding the recontext scripts
		refactorFiles(false);
		
		storeMetaDataFile("2", "recontext");
	}
	
	/**
	 * Create an ISO using its associated attributes and stored context data.
	 * 
	 * @return The ISO created.
	 * @throws SystemCallException
	 *             Thrown if the command to create the ISO via a system call
	 *             fails.
	 */
	public Iso create() throws SystemCallException {

		// Detect Linux distribution
		String commandName;
		if (new File("/etc/debian_version").exists()) {
			LOGGER.info("Debian distribution Variant detected using \"genisoimage\"");
			commandName = "genisoimage";
		} else if (new File("/etc/redhat-release").exists()) {
			LOGGER.info("Redhat distribution variant detected using \"mkisofs\"");
			commandName = "mkisofs";
		} else {
			LOGGER.info("Unknown linux distribution detected using default \"mkisofs\"");
			commandName = "mkisofs";
		}

		ArrayList<String> arguments = new ArrayList<String>();

		// Generate SUSP and RR records
		arguments.add("-R");
		// File ownership and modes
		arguments.add("-r");
		// Generate Joliet directory records
		arguments.add("-J");
		// Allow full 31 character filenames
		arguments.add("-l");
		arguments.add("-allow-leading-dots");
		arguments.add("-allow-lowercase");
		arguments.add("-allow-multidot");
		// filename
		arguments.add("-o");
		arguments.add(iso.getUri());
		arguments.add(isoDataDirectory);

		// Executed command looks like so:
		// "mkisofs -o iso.getFileName() isoDataDirectory"
		try {
			systemCall.runCommand(commandName, arguments);
		} catch (SystemCallException e) {
			if (configuration.isDefaultValues()) {
				LOGGER.warn(
						"Failed to run command, is this invocation in a unit test?",
						e);
			} else {
				throw e;
			}
		}

		if (systemCall.getReturnValue() == 0) {
			iso.setCreated(true);
			LOGGER.info("Iso created with uri: " + iso.getUri());
		} else {
			LOGGER.error("Iso Creation Failed! Return value was: "
					+ systemCall.getReturnValue());
		}

		// Print out the directory tree structure for debug purposes:
		LOGGER.debug("Files in directory: " + isoDataDirectory + File.separator);
		try {
			List<File> files = getFileListing(new File(isoDataDirectory));
			for (File file : files) {
				LOGGER.debug(file);
			}
		} catch (Exception e) {
			LOGGER.error("File was not found while listing directory!", e);
		}

		// Remove isoDataDirectory recursively after creating the ISO:
		try {
			deleteRecursive(new File(isoDataDirectory));
			LOGGER.debug("Recursively deleted isoDataDirectory: "
					+ isoDataDirectory + File.separator);
		} catch (FileNotFoundException e) {
			LOGGER.error("Cannot recursively delete isoDataDirectory: "
					+ isoDataDirectory, e);
		}

		return iso;
	}
	
	/**
	 * Delete a directory recursively, this does the equivalent of "rm -r".
	 * 
	 * TODO: move this to a utils class..
	 * 
	 * @param path
	 *            Root File Path.
	 * @return True if the file and all sub files/directories have been removed.
	 * @throws FileNotFoundException.
	 */
	private static boolean deleteRecursive(File path)
			throws FileNotFoundException {
		if (!path.exists()) {
			throw new FileNotFoundException(path.getAbsolutePath());
		}
		
		boolean ret = true;
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				ret = ret && deleteRecursive(f);
			}
		}
		return ret && path.delete();
	}

	/**
	 * Recursively walk a directory tree and return a List of all Files found;
	 * the List is sorted using File.compareTo().
	 * 
	 * TODO: move this to a utils class..
	 * 
	 * @param aStartingDir
	 *            is a valid directory, which can be read.
	 */
	private static List<File> getFileListing(File aStartingDir)
			throws FileNotFoundException {
		validateDirectory(aStartingDir);
		List<File> result = getFileListingNoSort(aStartingDir);
		Collections.sort(result);
		return result;
	}

	/**
	 * Get file list without sorting.
	 * 
	 * TODO: Move this to a utils class...
	 * 
	 * @param aStartingDir
	 *            The starting directory.
	 * @return List of files found.
	 * @throws FileNotFoundException
	 */
	private static List<File> getFileListingNoSort(File aStartingDir)
			throws FileNotFoundException {
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for (File file : filesDirs) {
			// always add, even if directory
			result.add(file);
			if (!file.isFile()) {
				// must be a directory
				// recursive call!
				List<File> deeperList = getFileListingNoSort(file);
				result.addAll(deeperList);
			}
		}
		return result;
	}

	/**
	 * Directory is valid if it exists, does not represent a file, and can be
	 * read.
	 * 
	 * TODO: Move this to a utils class...
	 */
	private static void validateDirectory(File aDirectory)
			throws FileNotFoundException {
		if (aDirectory == null) {
			throw new IllegalArgumentException("Directory should not be null.");
		}
		if (!aDirectory.exists()) {
			throw new FileNotFoundException("Directory does not exist: "
					+ aDirectory);
		}
		if (!aDirectory.isDirectory()) {
			throw new IllegalArgumentException("Is not a directory: "
					+ aDirectory);
		}
		if (!aDirectory.canRead()) {
			throw new IllegalArgumentException("Directory cannot be read: "
					+ aDirectory);
		}
	}

}
