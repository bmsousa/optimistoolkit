/*
 * Copyright (c) 2010-2013 British Telecom and City University London
 *
 * This file is part of BrokerVPNCredentials component of the WP 5.4
 * (Inter-Cloud Security) of the EU OPTIMIS project.
 *
 * BrokerVPNCredentials can be used under the terms of the SHARED SOURCE LICENSE
 * FOR NONCOMMERCIAL USE. 
 *
 * You should have received a copy of the SHARED SOURCE LICENSE FOR
 * NONCOMMERCIAL USE in the project's root directory. If not, please contact the
 * author at ali.sajjad@bt.com
 *
 * Author: Ali Sajjad
 *
 */
package eu.optimis.ics.BrokerVPNCredentials;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.cert.CertificateEncodingException;

public interface RemoteCSR extends Remote {
	
	public byte[] getCACertificate() throws RemoteException, IOException, CertificateEncodingException;
	
	public byte[] getSignedCertificateBytes(byte[] sentCSRBytes) throws RemoteException, IOException;
}
