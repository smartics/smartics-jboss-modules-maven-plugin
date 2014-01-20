/*
 * Copyright 2013-2014 smartics, Kronseder & Reiner GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.smartics.maven.plugin.jboss.modules.aether;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.AbstractRepositoryListener;
import org.sonatype.aether.RepositoryEvent;

/**
 * Uses the underlying logger to report on repository events.
 */
class RepositoryLogListener extends AbstractRepositoryListener
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The logger to log to.
   */
  private final Logger log;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  /**
   * Convenience constructor to use an internally created logger with the name
   * of this class.
   */
  public RepositoryLogListener()
  {
    this(LoggerFactory.getLogger(RepositoryLogListener.class));
  }

  /**
   * Default constructor.
   */
  public RepositoryLogListener(final Logger log)
  {
    this.log = log;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  // --- business -------------------------------------------------------------

  // ... descriptor handling ..................................................

  /**
   * {@inheritDoc}
   * <p>
   * Logs at warn level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#artifactDescriptorMissing(org.sonatype.aether.RepositoryEvent)
   */
  public void artifactDescriptorMissing(final RepositoryEvent event)
  {
    log.warn("Missing descriptor for artifact '{}'.", event.getArtifact());
  }

  /**
   * {@inheritDoc}
   * <p>
   * Logs at warn level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#artifactDescriptorInvalid(org.sonatype.aether.RepositoryEvent)
   */
  public void artifactDescriptorInvalid(final RepositoryEvent event)
  {
    log.warn("Invalid descriptor for artifact '{}': {}", event.getArtifact(),
        event.getException().getMessage());
  }

  // ... meta data resolving ..................................................

  /**
   * {@inheritDoc}
   * <p>
   * Logs at warn level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#metadataInvalid(org.sonatype.aether.RepositoryEvent)
   */
  public void metadataInvalid(final RepositoryEvent event)
  {
    log.warn("Invalid metadata: {}", event.getMetadata());
  }

  /**
   * {@inheritDoc}
   * <p>
   * Logs at debug level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#metadataResolving(org.sonatype.aether.RepositoryEvent)
   */
  public void metadataResolving(final RepositoryEvent event)
  {
    log.debug("Resolving metadata '{}' from '{}'.", event.getMetadata(),
        event.getRepository());
  }

  /**
   * {@inheritDoc}
   * <p>
   * Logs at debug level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#metadataResolved(org.sonatype.aether.RepositoryEvent)
   */
  public void metadataResolved(final RepositoryEvent event)
  {
    log.debug("Resolved metadata '{}' from '{}'.", event.getMetadata(),
        event.getRepository());
  }

  // ... artifact resolving ...................................................

  /**
   * {@inheritDoc}
   * <p>
   * Logs at debug level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#artifactResolving(org.sonatype.aether.RepositoryEvent)
   */
  public void artifactResolving(final RepositoryEvent event)
  {
    log.debug("Resolving artifact '{}'." + event.getArtifact());
  }

  /**
   * {@inheritDoc}
   * <p>
   * Logs at debug level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#artifactResolved(org.sonatype.aether.RepositoryEvent)
   */
  public void artifactResolved(final RepositoryEvent event)
  {
    log.debug("Resolved artifact '{}' from '{}'.", event.getArtifact(),
        event.getRepository());
  }

  // ... artifact downloading .................................................

  /**
   * {@inheritDoc}
   * <p>
   * Logs at debug level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#artifactDownloading(org.sonatype.aether.RepositoryEvent)
   */
  public void artifactDownloading(final RepositoryEvent event)
  {
    log.debug("Downloading artifact '{}' from '{}'.", event.getArtifact(),
        event.getRepository());
  }

  /**
   * {@inheritDoc}
   * <p>
   * Logs at debug level.
   * </p>
   *
   * @see org.sonatype.aether.AbstractRepositoryListener#artifactDownloaded(org.sonatype.aether.RepositoryEvent)
   */
  public void artifactDownloaded(final RepositoryEvent event)
  {
    log.debug("Downloaded artifact '{}' from '{}'.", event.getArtifact(),
        event.getRepository());
  }

  // --- object basics --------------------------------------------------------

}
