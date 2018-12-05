package monash.ml.tools.arff;

import static monash.ml.tools.arff.Utility.*;

import java.io.IOException;
import java.nio.file.Path;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/** Helper working with Arff files and weka classes */
public class ArffFile implements ArffReaderFactory {

	// --- --- --- Fields
	private final int numData;
	private final Path source;
	private final Instances structure;

	// --- --- --- Constructors
	/** Will open a reader on the file to compute the structure and the number of data. Will read the whole file. */
	public ArffFile(Path source) throws IOException {
		// First, initialize the structure
		this.source = source;
		// Then use our own method to get a reader.
		ArffReader reader = getNewReader();
		// Get the rest of the info through the reader
		this.structure = reader.getStructure();
		this.structure.setClassIndex(structure.numAttributes() - 1); // Set the class index. Last attribute.
		// Count the number of data
		this.numData = countInstance(reader);
	}
	
	/** To be used if you already knows the structure and the number of data.
	 *  The data are stored as provided (eg. no 'setClassIndex' done on the structure').
	 *  */
	public ArffFile(Path source, Instances structure, int numData) {
		this.source = source;
		this.structure = structure;
		this.numData = numData;
	}

	// --- --- --- Methods
	
	public Path getPath() {
		return source;
	}

	/** Create a new ArffReader for the associated file */
	@Override
	public ArffReader getNewReader() throws IOException {
		return getArffReader(source);
	}

	@Override
	public int getNumData() {
		return numData;
	}

	@Override
	public Instances getStructure() {
		return structure;
	}

	@Override
	public String getDatasetName() {
		return source.getFileName().toString();
	}

}
