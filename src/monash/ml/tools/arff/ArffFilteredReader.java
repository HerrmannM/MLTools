package monash.ml.tools.arff;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Predicate;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

public class ArffFilteredReader extends ArffReader {
	
	// --- --- --- Fields
	private final Predicate<Integer> keep;
	private int currentIndex;

	// --- --- --- Constructor
	
	/** ArffFilteredReader keeping an instance based on its index. */ 
	public ArffFilteredReader(Reader reader, int capacity, Predicate<Integer> keep) throws IOException {
		super(reader, capacity);
		this.keep = keep;
		this.currentIndex = 0;
	}
	
	// --- --- --- Access methods	
	
	@Override
	/** Override readInstance from weka.
	 * Warning: readInstance(Instances structure) is a shorthand for readInstance(structure, true).
	 *           We only override this one. */
	public Instance readInstance(Instances structure, boolean flag) throws IOException {
		Instance i = super.readInstance(structure, flag);
		while (i != null && !keep.test(currentIndex)) { // Skip if we do not keep the data
			i = super.readInstance(structure, flag);
			currentIndex++;
		}
		currentIndex++;
		return i;
	}

	// --- --- --- Disable other kind of access
	@Override
	public Instances getData() {
		throw new UnsupportedOperationException(
				"ArffReaderKeep: only usable with 'readInstance(Instances structure)'");
	}
}
