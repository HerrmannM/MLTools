package monash.ml.tools.arff;

import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/**
 * Interface abstracting the creation of an ArffReader. Also, the factory knows
 * some property about the underlying dataset.
 */
public interface ArffReaderFactory {

	// --- --- --- Mandatory override

	/** Get a new ArffReader */
	public ArffReader getNewReader() throws IOException;

	// --- --- --- Optional override

	/** Get the number of data (number of records/instances) in the dataset */
	public default int getNumData() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	/** Get the structure of the dataset */
	public default Instances getStructure() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	/** Get the dataset name */
	public default String getDatasetName() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	// --- --- --- Default implementation

	/** Get the number of attributes in the dataset */
	public default int getNumAttributes() {
		return getStructure().numAttributes();
	}

	/** Get the number of classes in the dataset */
	public default int getNumClasses() {
		return getStructure().numClasses();
	}
}