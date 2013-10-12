package de.smartics.maven.plugin.jboss.modules.parser;

import org.jdom2.Element;

/**
 * Adds clusions.
 *
 * @param <T> the type of clusion to be added.
 */
interface ClusionAdder<T>
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // ****************************** Initializer *******************************

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- get&set --------------------------------------------------------------

  /**
   * Returns the identifier of the element that encloses the collection of
   * clusions.
   *
   * @return the identifier of the element that encloses the collection of
   *         clusions.
   */
  String getCollectionElementId();

  /**
   * Returns the identifier of the element that encloses the clusion.
   *
   * @return the identifier of the element that encloses the clusion.
   */
  String getElementId();

  // --- business -------------------------------------------------------------

  /**
   * Adds the clusion.
   *
   * @param clusion the clusion to be added.
   */
  void add(T clusion);

  /**
   * Adds all clusions of the given {@link #getCollectionElementId()
   * collectionElementId} with in the {@code rootElement}.
   *
   * @param rootElement the element within the {@link #getCollectionElementId()
   *          collectionElement}s are found.
   */
  void addClusions(Element rootElement);

  // --- object basics --------------------------------------------------------

}
