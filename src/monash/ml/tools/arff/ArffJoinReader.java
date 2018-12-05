package monash.ml.tools.arff;

import java.io.IOException;
import java.io.Reader;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

public class ArffJoinReader extends ArffReader {
	
	// --- --- --- Fields
	private final Reader[] readers;
	private ArffReader currentReader;
	private int currentIdx;

	// --- --- --- Constructor

	/** An ArffJoinReader is built over a collection of Reader.
	 * Note; internal code is ugly, but we do not have the choice because of the design of weka...
	 * @throws IOException 
	 */ 
	public ArffJoinReader(Reader[] readers, Instances structure) throws IOException {
		super(readers[0], Utility.CAPACITY);
		this.readers = readers;
		currentReader = this;
		currentIdx = 0;
	}

	// --- --- --- Access methods

	@Override
	/**
	 * Override readInstance from weka. Warning: readInstance(Instances structure)
	 * is a shorthand for readInstance(structure, true). We only override this one.
	 */
	public Instance readInstance(Instances structure, boolean flag) throws IOException {
		Instance i = null;
		if(currentReader == this) {
			i = super.getInstance(structure, flag);
		} else {
			i = currentReader.readInstance(structure, flag);
		}
		if(i==null && currentIdx < readers.length -1) {
			currentIdx++;
			currentReader = new ArffReader(readers[currentIdx], Utility.CAPACITY);
			i = currentReader.readInstance(structure, flag);
		}
		return i;
	}

	// --- --- --- Disable other kind of accesses
	@Override
	public Instances getData() {
		throw new UnsupportedOperationException("ArffReaderKeep: only usable with 'readInstance(Instances structure)'");
	}
}
