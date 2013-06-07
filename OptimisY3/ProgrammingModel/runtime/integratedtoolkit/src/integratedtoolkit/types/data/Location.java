/*
 *  Copyright 2002-2013 Barcelona Supercomputing Center (www.bsc.es)
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
package integratedtoolkit.types.data;

import java.io.Serializable;

public class Location implements Serializable, Comparable<Location> {

    private String host;
    private String path;

    /* Alternative implementation:
     * - Disk name
     * - Relative path
     */
    public Location() {
    }

    public Location(String host, String path) {
        this.host = host;
        this.path = path;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // Comparable interface implementation
    public int compareTo(Location loc) throws NullPointerException {
        if (loc == null) {
            throw new NullPointerException();
        }

        int compHost;
        // First compare hosts
        if ((compHost = this.getHost().compareTo(((Location) loc).getHost())) != 0) {
            return compHost;
        } // If same host, compare paths
        else {
            return this.getPath().compareTo(((Location) loc).getPath());
        }
    }

    // Override the toString method
    public String toString() {
        return host + ":" + path;
    }
}