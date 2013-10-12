package de.smartics.maven.plugin.jboss.modules.parser;

/**
 * Base implementation of an {@link ClusionAdder}.
 *
 * @param <T> the type of clusion to be added.
 */
abstract class AbstractClusionAdder<T> implements ClusionAdder<T>
{
  // ********************************* Fields *********************************

  // --- constants ------------------------------------------------------------

  // --- members --------------------------------------------------------------

  /**
   * The identifier of the element that encloses the collection of clusions.
   */
  protected final String collectionElementId;

  /**
   * The identifier of the element that encloses the clusion.
   */
  protected final String elementId;

  // ****************************** Initializer *******************************

  // ****************************** Constructors ******************************

  AbstractClusionAdder(final String collectionElementId, final String elementId)
  {
    assert collectionElementId != null : "The collectionElementId must not be 'null'.";
    assert elementId != null : "The elementId must not be 'null'.";

    this.collectionElementId = collectionElementId;
    this.elementId = elementId;
  }

  // ****************************** Inner Classes *****************************

  // ********************************* Methods ********************************

  // --- init -----------------------------------------------------------------

  // --- get&set --------------------------------------------------------------

  @Override
  public String getCollectionElementId()
  {
    return collectionElementId;
  }

  @Override
  public String getElementId()
  {
    return elementId;
  }

  // --- business -------------------------------------------------------------

  // --- object basics --------------------------------------------------------

}
