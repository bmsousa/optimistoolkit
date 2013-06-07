package eu.optimis.elasticityengine.manifest;

import java.util.List;

/**
 * Object structure to be parsed from manifest
 * 
 * @author Daniel Espling (<a href="mailto:espling@cs.umu.se">espling@cs.umu.se</a>)
 *Copyright (C) 2012 Umeå University

* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
public class Scope {
    @Override
    public String toString() {
        return "Scope [imageIDs=" + imageIDs + "]";
    }

    private List<String> imageIDs;

    public List<String> getimageIDs() {
        return imageIDs;
    }

    public void setImageIDs(List<String> imageIDs) {
        this.imageIDs = imageIDs;
    }
}
