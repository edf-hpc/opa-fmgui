/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: DeviceCategoryType.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/08/17 18:49:16  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - change backend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/11 18:03:25  fernande
 *  Archive Log:    Support for generic preferences: a new node (Preferences) in the UserOptions XML now allows to define groups of preferences (Section) and key/value pairs (Entry) that are stored in Properties objects are runtime.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/13 21:01:48  fernande
 *  Archive Log:    Added support for valueHeader attribute in UserOptions XML.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/29 18:51:55  fernande
 *  Archive Log:    Adding UserOptions XML and  saving it to the database. Includes XML schema validation.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.xml;

import javax.xml.bind.TypeConstraintException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import com.intel.stl.api.configuration.ResourceCategory;
import com.intel.stl.api.configuration.ResourceType;

/**
 * <p>
 * Java class for ResourceCategoryType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceCategoryType">
 *   &lt;simpleContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceCategoryType", propOrder = { "value" })
@XmlSeeAlso({ PortCategoryType.class, HfiCategoryType.class,
        SwitchCategoryType.class })
public abstract class ResourceCategoryType {

    @XmlValue
    protected String value;

    @XmlAttribute
    protected Boolean showHeader;

    @XmlAttribute
    protected String valueHeader;

    /**
     * Gets the value of the value property. This value is used as the key
     * header for this resource category
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property. This value is used as the key
     * header for this resource category
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the showHeader property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public boolean isShowHeader() {
        if (showHeader == null) {
            return true;
        } else {
            return showHeader;
        }
    }

    /**
     * Sets the value of the showHeader property.
     * 
     * @param value
     *            allowed object is {@link boolean }
     * 
     */
    public void setShowHeader(Boolean value) {
        this.showHeader = value;
    }

    /**
     * Gets the value of the valueHeader property. This value is used as the
     * value header for this resource category
     * 
     * @return possible object is {@link String }
     * 
     */

    public String getValueHeader() {
        return valueHeader;
    }

    /**
     * Sets the value of the valueHeader property. This value is used as the
     * value header for this resource category
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setValueHeader(String value) {
        this.valueHeader = value;
    }

    /**
     * Gets the corresponding resource category for this ResourceCategoryType
     * 
     * @return possible object is {@link ResourceCategory }
     * 
     */
    protected abstract ResourceCategory getResourceCategory();

    protected ResourceCategory getPropertyCategoryFor(
            ResourceType resourceType, String categoryName) {
        ResourceCategory category =
                ResourceCategory.getResourceCategoryFor(categoryName);
        if (category == null) {
            TypeConstraintException tce =
                    new TypeConstraintException("Category name '"
                            + categoryName + "' (" + resourceType.name()
                            + ") has no matching PropertyCategory");
            throw tce;
        }
        if (!category.isApplicableTo(resourceType)) {
            TypeConstraintException tce =
                    new TypeConstraintException("Category name '"
                            + categoryName
                            + "' is not applicable to resource type "
                            + resourceType.name());
            throw tce;
        }
        return category;
    }

}
