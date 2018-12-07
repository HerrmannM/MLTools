package mltools.arff;

import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.function.Predicate;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.Saver;
import weka.core.converters.ArffLoader.ArffReader;

/**
 * Multi filter. Contrary to an ArffFilter a MultiFilter does not extends
 * ArffReader.
 */
public class ArffMultiFilteredReader {

	// --- --- --- Fields
	private final Predicate<Integer>[] keep;
	private final int plength;
	private final ArffReader reader;
	private final Result result;
	private final Instances structure;

	private int currentIndex;

	// --- --- --- Constructor

	/** ArffFilteredReader keeping an instance based on its index. Reuse the structure from the factory. */
	public ArffMultiFilteredReader(ArffReaderFactory readerFactory, Predicate<Integer>[] keep) throws IOException {
		this.keep = keep;
		this.plength = keep.length;
		this.reader = readerFactory.getNewReader();
		this.currentIndex = 0;
		this.result = new Result(null, plength);
		this.structure = readerFactory.getStructure();
	}

	// --- --- --- Access methods

	public static class Result {
		public BitSet set;
		public Instance instance;

		public Result(Instance inst, int setSize) {
			set = new BitSet(setSize);
			instance = inst;
		}
	}

	/**
	 * Shorthand for readInstance(structure, true), mimicking the one from
	 * ArffReader.
	 */
	public Result readInstance(Instances structure) throws IOException {
		return readInstance(structure, true);
	}

	/**
	 * Put in the result the index of the predicate matching the instance's index.
	 * If there is no more instance in the reader, return null. Else, the result is
	 * not null and result.instance is guaranteed to be non-null. Warning: to limit
	 * object allocation, the same Result object and its BitSet field are reused!
	 * Copy the bitset if you want to store it. The instance will be the new one
	 * from the reader.
	 */
	public Result readInstance(Instances structure, boolean flag) throws IOException {
		Instance inst = reader.readInstance(structure, flag);
		if (inst != null) {
			result.set.clear();
			// Test the predicates
			for (int i = 0; i < plength; ++i) {
				result.set.set(i, keep[i].test(currentIndex));
			}
			result.instance = inst;
			currentIndex++;
			return result;
		} else {
			return null;
		}
	}

	public Instances getStructure() {
		return structure;
	}

	public int getFilterNumber() {
		return plength;
	}

	// --- --- --- Write to files

	/**
	 * Write the content of the provided reader to temporary files according to a
	 * predicate per file. Should not have been use before!
	 */
	public ArffFile[] multiFilteredToFiles(String prefix) throws IOException, IllegalStateException {

		if (currentIndex != 0) {
			throw new IllegalStateException("Method cannot be called if some instances have already been read.");
		}

		File out[] = new File[plength];
		ArffSaver saver[] = new ArffSaver[plength];
		int numData[] = new int[plength];

		// Init our arrays, prepare the files
		for (int i = 0; i < plength; ++i) {
			File f = File.createTempFile(prefix + "-" + i + "-", ".arff");
			// f.deleteOnExit();
			out[i]=f;
			System.out.println("Prepare file " + (i+1) + "/" + plength + ": " + f.toString());

			ArffSaver s = new ArffSaver();
			s.setFile(f);
			s.setRetrieval(Saver.INCREMENTAL);
			s.setStructure(structure);
			saver[i] = s;

			numData[i] = 0;
		}

		// Read the data
		Instance inst;
		while ((inst = reader.readInstance(structure)) != null) {
			// Test the predicates
			for (int iChunk = 0; iChunk < plength; ++iChunk) {
				if (keep[iChunk].test(currentIndex)) {
					// For each verified predicate: add the instance to the saver, increment the
					// count of numData
					saver[iChunk].writeIncremental(inst);
					numData[iChunk]++;
					break;
				}
			}
			currentIndex++;
		}
		for (int i = 0; i < plength; ++i) {
			System.out.println("File " + (i+1) + "/" + plength + " contains " +numData[i] + " items"); 
		}

		// Finish: must call saver[i].writeIncremental(null) to terminate the incremental
		// saving process.
		ArffFile result[] = new ArffFile[plength];
		for (int i = 0; i < plength; ++i) {
			saver[i].writeIncremental(null);
			result[i] = new ArffFile(out[i].toPath(), structure, numData[i]);
		}

		return result;
	}
}