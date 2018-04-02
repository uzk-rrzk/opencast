/**
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 *
 * The Apereo Foundation licenses this file to you under the Educational
 * Community License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at:
 *
 *   http://opensource.org/licenses/ecl2.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package org.opencastproject.metadata.dublincore;

import static java.util.UUID.randomUUID;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
import static org.opencastproject.mediapackage.XMLCatalogImpl.XSI_NS_PREFIX;
import static org.opencastproject.metadata.dublincore.DublinCore.ELEMENTS_1_1_NS_PREFIX;
import static org.opencastproject.metadata.dublincore.DublinCore.ELEMENTS_1_1_NS_URI;
import static org.opencastproject.metadata.dublincore.DublinCore.TERMS_NS_PREFIX;
import static org.opencastproject.metadata.dublincore.DublinCore.TERMS_NS_URI;

import org.opencastproject.mediapackage.EName;
import org.opencastproject.mediapackage.MediaPackageElementFlavor;
import org.opencastproject.mediapackage.MediaPackageElements;
import org.opencastproject.metadata.dublincore.OpencastDctermsDublinCore.Episode;
import org.opencastproject.metadata.dublincore.OpencastDctermsDublinCore.Series;
import org.opencastproject.util.XmlNamespaceBinding;
import org.opencastproject.util.XmlNamespaceContext;

import com.entwinemedia.fn.data.Opt;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.xml.XMLConstants;

/**
 * Factory for metadata catalogs following the DublinCore standard.
 */
@ParametersAreNonnullByDefault
public final class DublinCores {
  /**
   * Namespace name of Dublin Core metadata generated by Opencast. By default this namespace is the default namespace
   * of xml documents generated by this class.
   */
  public static final String OC_DC_CATALOG_NS_URI = "http://www.opencastproject.org/xsd/1.0/dublincore/";

  /** The dc root element of Opencast DublinCore catalogs. */
  public static final EName OC_DC_CATALOG_ROOT_ELEMENT = new EName(OC_DC_CATALOG_NS_URI, "dublincore");

  /** Namespace URI for Opencast properties. */
  public static final String OC_PROPERTY_NS_URI = "http://www.opencastproject.org/matterhorn/";

  /** Prefix for Opencast properties. */
  public static final String OC_PROPERTY_NS_PREFIX = "oc";

  /**
   * Opencast property: The timezone of the agent specified to be scheduled with an event. IE: "America/Chicago"
   */
  public static final EName OC_PROPERTY_AGENT_TIMEZONE = new EName(OC_PROPERTY_NS_URI, "agentTimezone");

  /**
   * This is a string defining a recurrence pattern as specified in RFC 2445. See <a
   * href="http://tools.ietf.org/html/rfc2445#section-4.8.5.4">http://tools.ietf.org/html/rfc2445#section-4.8.5.4</a>
   */
  public static final EName OC_PROPERTY_RECURRENCE = new EName(OC_PROPERTY_NS_URI, "recurrence");
  public static final EName OC_PROPERTY_ANNOTATION = new EName(OC_PROPERTY_NS_URI, "annotation");
  public static final EName OC_PROPERTY_ADVERTISED = new EName(OC_PROPERTY_NS_URI, "advertised");
  public static final EName OC_PROPERTY_PROMOTED = new EName(OC_PROPERTY_NS_URI, "promoted");
  public static final EName OC_PROPERTY_DURATION = new EName(OC_PROPERTY_NS_URI, "duration");

  private DublinCores() {
  }

  /**
   * Create a new Opencast DublinCore metadata catalog for episodes.
   * <ul>
   * <li>Set flavor to {@link org.opencastproject.mediapackage.MediaPackageElements#EPISODE}.
   * <li>Register all necessary namespaces and set the root tag to {@link #OC_DC_CATALOG_ROOT_ELEMENT}.
   * <li>The catalog does not have an {@linkplain DublinCoreCatalog#getIdentifier() identifier} and the
   *   {@link DublinCore#PROPERTY_IDENTIFIER identifier} property is not set.
   * </ul>
   *
   * @deprecated use {@link #mkOpencastEpisode()} instead
   */
  @Nonnull
  @Deprecated
  public static Episode mkOpencast() {
    return mkOpencastEpisode();
  }

  /**
   * Create a new Opencast DublinCore metadata catalog for episodes.
   * <ul>
   * <li>Set flavor to {@link org.opencastproject.mediapackage.MediaPackageElements#EPISODE}.
   * <li>Register all necessary namespaces and set the root tag to {@link #OC_DC_CATALOG_ROOT_ELEMENT}.
   * <li>The catalog does not have an {@linkplain DublinCoreCatalog#getIdentifier() identifier} and the
   *   {@link DublinCore#PROPERTY_IDENTIFIER identifier} property is not set.
   * </ul>
   */
  @Nonnull
  public static Episode mkOpencastEpisode() {
    return new Episode(mkOpencast(MediaPackageElements.EPISODE));
  }

  /**
   * Create a new Opencast DublinCore metadata catalog for episodes.
   * <ul>
   * <li>Set flavor to {@link org.opencastproject.mediapackage.MediaPackageElements#EPISODE}.
   * <li>Register all necessary namespaces and set the root tag to {@link #OC_DC_CATALOG_ROOT_ELEMENT}.
   * <li>Set the {@link DublinCore#PROPERTY_IDENTIFIER dcterms:identifier} to {@code id}
   * <li>The catalog does not have an {@linkplain DublinCoreCatalog#getIdentifier() identifier}
   * </ul>
   */
  @Nonnull
  public static Episode mkOpencastEpisode(String id) {
    final Episode dc = mkOpencastEpisode();
    dc.setDcIdentifier(id);
    return dc;
  }

  /**
   * Create a new Opencast DublinCore metadata catalog for episodes.
   * <ul>
   * <li>Set flavor to {@link org.opencastproject.mediapackage.MediaPackageElements#EPISODE}.
   * <li>Register all necessary namespaces and set the root tag to {@link #OC_DC_CATALOG_ROOT_ELEMENT}.
   * <li>Set the {@link DublinCore#PROPERTY_IDENTIFIER dcterms:identifier} to {@code id} if some, or create a random
   *   UUID otherwise
   * <li>The catalog does not have an {@linkplain DublinCoreCatalog#getIdentifier() identifier}
   * </ul>
   */
  @Nonnull
  public static Episode mkOpencastEpisode(Opt<String> id) {
    final Episode dc = mkOpencastEpisode();
    dc.setDcIdentifier(id.getOr(randomUUID().toString()));
    return dc;
  }

  /**
   * Create a new Opencast DublinCore metadata catalog for episodes.
   * <ul>
   * <li>Set flavor to {@link org.opencastproject.mediapackage.MediaPackageElements#EPISODE}.
   * <li>Register all necessary namespaces and set the root tag to {@link #OC_DC_CATALOG_ROOT_ELEMENT}.
   * <li>Set the {@link DublinCore#PROPERTY_IDENTIFIER dcterms:identifier} to {@code id}
   * <li>Link to series {@code seriesId} setting the {@link DublinCore#PROPERTY_IS_PART_OF} property.
   * <li>The catalog does not have an {@linkplain DublinCoreCatalog#getIdentifier() identifier}
   * </ul>
   */
  @Nonnull
  public static Episode mkOpencastEpisode(String id, String seriesId) {
    final Episode dc = mkOpencastEpisode(id);
    dc.setIsPartOf(seriesId);
    return dc;
  }

  /**
   * Create an Opencast episode DublinCore accessor for a {@link DublinCoreCatalog}.
   * Read and write operations access and modify the wrapped catalog.
   */
  @Nonnull
  public static Episode mkOpencastEpisode(DublinCoreCatalog dc) {
    return new Episode(dc);
  }

  /**
   * Create a new Opencast DublinCore metadata catalog for series.
   * <ul>
   * <li>Set flavor to {@link org.opencastproject.mediapackage.MediaPackageElements#SERIES}.
   * <li>Register all necessary namespaces and set the root tag to {@link #OC_DC_CATALOG_ROOT_ELEMENT}.
   * <li>The catalog does not have an {@linkplain DublinCoreCatalog#getIdentifier() identifier} and the
   *   {@link DublinCore#PROPERTY_IDENTIFIER identifier} property is not set.
   * </ul>
   */
  @Nonnull
  public static Series mkOpencastSeries() {
    return new Series(mkOpencast(MediaPackageElements.SERIES));
  }

  /**
   * Create a new Opencast DublinCore metadata catalog for series.
   * <ul>
   * <li>Set flavor to {@link org.opencastproject.mediapackage.MediaPackageElements#SERIES}.
   * <li>Register all necessary namespaces and set the root tag to {@link #OC_DC_CATALOG_ROOT_ELEMENT}.
   * <li>Set the {@link DublinCore#PROPERTY_IDENTIFIER dcterms:identifier} to {@code id}
   * <li>The catalog does not have an {@linkplain DublinCoreCatalog#getIdentifier() identifier}
   * </ul>
   */
  @Nonnull
  public static Series mkOpencastSeries(String id) {
    final Series dc = mkOpencastSeries();
    dc.setDcIdentifier(id);
    return dc;
  }

  /**
   * Create an Opencast series DublinCore accessor for a {@link DublinCoreCatalog}.
   * Read and write operations access and modify the wrapped catalog.
   */
  @Nonnull
  public static Series mkOpencastSeries(DublinCoreCatalog dc) {
    return new Series(dc);
  }

  /**
   * Create a new empty catalog suitable to take properties from the standard DublinCore
   * namespaces {@link DublinCore#ELEMENTS_1_1_NS_URI} and {@link DublinCore#TERMS_NS_URI}.
   * <p>
   * Please note that neither a flavor nor a root tag is set.
   */
  @Nonnull
  public static DublinCoreCatalog mkStandard() {
    final DublinCoreCatalog dc = new DublinCoreCatalog();
    dc.addBindings(XmlNamespaceContext.mk(
            XmlNamespaceBinding.mk(ELEMENTS_1_1_NS_PREFIX, ELEMENTS_1_1_NS_URI),
            XmlNamespaceBinding.mk(TERMS_NS_PREFIX, TERMS_NS_URI)));
    return dc;
  }

  /** Create a new empty catalog with no special namespace registered, no root tag and no flavor. */
  @Nonnull
  public static DublinCoreCatalog mkSimple() {
    return new DublinCoreCatalog();
  }

  /**
   * Read a DublinCore catalog from a stream containing either JSON or XML. The method is
   * capable of detecting the used format.
   * <p>
   * The reader is not capable of determining the catalog's flavor.
   * <p>
   * <strong>Implementation note:</strong> In order to detect the format the whole stream is read into memory first. If you
   * know upfront whether JSON or XML is used you may want to choose {@link DublinCoreJsonFormat#read(java.io.InputStream)}
   * or {@link DublinCoreXmlFormat#read(java.io.InputStream)} for performance reasons.
   */
  @Nonnull
  public static DublinCoreCatalog read(InputStream in) {
    final String ser;
    try {
      ser = IOUtils.toString(in, "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException("Unable to read DublinCore from stream", e);
    }
    if (ser.startsWith("{")) {
      try {
        return DublinCoreJsonFormat.read(ser);
      } catch (Exception e) {
        throw new RuntimeException("Unable to read DublinCore catalog, JSON parsing failed.", e);
      }
    } else {
      try {
        return DublinCoreXmlFormat.read(ser);
      } catch (Exception e) {
        throw new RuntimeException("Unable to read DublinCore catalog, XML parsing failed.", e);
      }
    }
  }

  private static DublinCoreCatalog mkOpencast(MediaPackageElementFlavor flavor) {
    final DublinCoreCatalog dc = mkStandard();
    dc.setFlavor(flavor);
    dc.addBindings(XmlNamespaceContext.mk(
            // Opencast property namespace
            XmlNamespaceBinding.mk(OC_PROPERTY_NS_PREFIX, OC_PROPERTY_NS_URI),
            // Opencast root tag
            XmlNamespaceBinding.mk(XMLConstants.DEFAULT_NS_PREFIX, OC_DC_CATALOG_NS_URI)));
            //XMLSchema-instance namespace xsi
            XmlNamespaceBinding.mk(XSI_NS_PREFIX, W3C_XML_SCHEMA_INSTANCE_NS_URI);
    dc.setRootTag(OC_DC_CATALOG_ROOT_ELEMENT);
    return dc;
  }
}